package controllers.bootstrap

import database.repos._
import database.repos.abstracts.Create
import models._
import play.api.libs.json._
import play.api.libs.ws.WSClient
import play.api.mvc.{AbstractController, ControllerComponents}
import service.TeachingUnitService

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext

@Singleton
final class MocogiBootstrapController @Inject() (
    cc: ControllerComponents,
    ws: WSClient,
    facultyRepository: FacultyRepository,
    degreeRepository: DegreeRepository,
    studyProgramRepository: StudyProgramRepository,
    languageRepository: LanguageRepository,
    seasonRepository: SeasonRepository,
    identityRepository: IdentityRepository,
    moduleRepository: ModuleRepository,
    moduleInStudyProgramRepository: ModuleInStudyProgramRepository,
    moduleSupervisorRepository: ModuleSupervisorRepository,
    moduleRelationRepository: ModuleRelationRepository,
    teachingUnitService: TeachingUnitService,
    specializationRepository: SpecializationRepository,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc) {
  private val url = "http://lwivs49.gm.fh-koeln.de:9001"

  def createFaculties = Action.async { _ =>
    create("faculties", facultyRepository)
  }

  def createDegrees = Action.async { _ =>
    create("degrees", degreeRepository)
  }

  def createLanguages = Action.async { _ =>
    create("languages", languageRepository)
  }

  def createSeasons = Action.async { _ =>
    create("seasons", seasonRepository)
  }

  def createIdentities = Action.async { _ =>
    create("identities", identityRepository)
  }

  def createStudyPrograms = Action.async { _ =>
    def makeStudyPrograms(
        sps: List[StudyProgramMocogi],
        tus: Seq[TeachingUnit]
    ) = {
      val studyPrograms = ListBuffer[StudyProgram]()
      val specializations = ListBuffer[Specialization]()
      sps.foreach { sp =>
        studyPrograms += StudyProgram(
          UUID.randomUUID(),
          tuMapping(sp.id, tus),
          sp.degree.id,
          sp.deLabel,
          sp.enLabel,
          "TODO",
          sp.po.id,
          sp.po.version,
          sp.specialization.map(_.id)
        )
        sp.specialization.collect {
          case spec if !specializations.exists(_.id == spec.id) =>
            specializations += Specialization(spec.id, spec.deLabel)
        }
      }
      (studyPrograms.toList, specializations.toList)
    }

    for {
      sps <- ws
        .url(s"$url/studyPrograms?extend=true")
        .get()
        .map(_.json.validate[List[StudyProgramMocogi]].get)
      tus <- teachingUnitService.all()
      (studyPrograms, specs) = makeStudyPrograms(sps, tus)
      ys <- specializationRepository.createOrUpdateMany(specs)
      xs <- studyProgramRepository.createOrUpdateMany(studyPrograms)
    } yield {
      val (createdSps, updatedSps) = xs.partition(_.isDefined)
      Ok(
        Json.obj(
          "createdMods" -> createdSps.size,
          "updatedMods" -> updatedSps.size,
          "createdModsInSp" -> ys.size
        )
      )
    }
  }

  def createModules = Action.async { _ =>
    def makeModules(ms: List[MocogiModule], sps: Seq[StudyProgram]) = {
      val modules = ListBuffer[Module]()
      val modulesInStudyProgram = ListBuffer[ModuleInStudyProgram]()
      val moduleSupervisor = ListBuffer[ModuleSupervisor]()
      val moduleRelations = ListBuffer[ModuleRelation]()

      ms.foreach { m =>
        modules += Module(
          m.id,
          m.metadata.title,
          m.metadata.abbrev,
          m.metadata.language,
          m.metadata.season,
          m.metadata.workload.collect {
            case (k, v)
                if (k == "lecture" | k == "seminar" | k == "practical" | k == "exercise") && v > 0 =>
              CourseId(k)
          }.toList
        )

        m.metadata.po.mandatory.foreach { po =>
          val sp = sps.find { sp =>
            po.specialization match {
              case Some(spec) => sp.specializationId.contains(spec)
              case None       => sp.poId == po.po
            }
          }.get
          modulesInStudyProgram += ModuleInStudyProgram(
            UUID.randomUUID(),
            m.id,
            sp.id,
            mandatory = true,
            focus = false,
            po.recommendedSemester
          )
        }

        m.metadata.po.optional.foreach { po =>
          val sp = sps.find { sp =>
            po.specialization match {
              case Some(spec) => sp.specializationId.contains(spec)
              case None       => sp.poId == po.po
            }
          }.get
          modulesInStudyProgram += ModuleInStudyProgram(
            UUID.randomUUID(),
            m.id,
            sp.id,
            mandatory = false,
            focus = false,
            // TODO add when mocogi supports it
//            focus = po.isFocus,
            po.recommendedSemester
          )
        }

        m.metadata.moduleManagement.foreach { person =>
          moduleSupervisor += ModuleSupervisor(m.id, person)
        }

        m.metadata.moduleRelation.collect {
          case MocogiModuleRelation.Parent(_, children) =>
            children.foreach(c => moduleRelations += ModuleRelation(m.id, c))
        }
      }

      (
        modules.toList,
        modulesInStudyProgram.toList,
        moduleSupervisor.toList,
        moduleRelations.toList
      )
    }

    for {
      ms <- ws
        .url(s"$url/modules?select=metadata")
        .get()
        .map(
          _.json
            .validate[List[MocogiModule]] match {
            case JsSuccess(value, _) =>
              value.filter(a =>
                a.metadata.status == "active" && a.metadata.moduleType == "module"
              )
            case JsError(errors) =>
              println(errors.mkString("\n"))
              Nil
          }
        ) if ms.nonEmpty
      sps <- studyProgramRepository.all()
      (modules, modulesInStudyPrograms, moduleSupervisor, moduleRelations) =
        makeModules(ms, sps)
      _ <- moduleInStudyProgramRepository.deleteAll()
      _ <- moduleRepository.deleteAll()
      xs <- moduleRepository.createOrUpdateMany(modules)
      ys <- moduleInStudyProgramRepository.createMany(modulesInStudyPrograms)
      zs <- moduleSupervisorRepository.createOrUpdateMany(moduleSupervisor)
      as <- moduleRelationRepository.createOrUpdateMany(moduleRelations)
    } yield {
      val (createdMods, updatedMods) = xs.partition(_.isDefined)
      Ok(
        Json.obj(
          "createdMods" -> createdMods.size,
          "updatedMods" -> updatedMods.size,
          "createdModsInSps" -> ys.size,
          "createdModsSups" -> zs.size,
          "createdModsRels" -> as.size
        )
      )
    }
  }

  def tuMapping(sp: String, tus: Seq[TeachingUnit]): UUID =
    (if (sp.startsWith("inf"))
       tus.find(_.deLabel == "Informatik")
     else tus.find(_.deLabel == "Ingenieurwesen")).get.id

  private def create[A <: UniqueEntity[_]](
      resource: String,
      repo: Create[_, A, _]
  )(implicit reads: Reads[A]) =
    for {
      resp <- ws.url(s"$url/$resource").get()
      _ = println(resp.json)
      elems = resp.json.validate[List[A]].get
      xs <- repo.createOrUpdateMany(elems)
    } yield {
      val (created, updated) = xs.partition(_.isDefined)
      Ok(Json.obj("createdMods" -> created.size, "updatedMods" -> updated.size))
    }

  implicit def facultyReads: Reads[Faculty] =
    (json: JsValue) =>
      for {
        abbrev <- json.\("id").validate[String]
        deLabel <- json.\("deLabel").validate[String]
        enLabel <- json.\("enLabel").validate[String]
      } yield Faculty(abbrev, deLabel, enLabel)

  implicit def gradeReads: Reads[Degree] =
    (json: JsValue) =>
      for {
        abbrev <- json.\("id").validate[String]
        deLabel <- json.\("deLabel").validate[String]
        enLabel <- json.\("enLabel").validate[String]
        deDesc <- json.\("deDesc").validate[String]
        enDesc <- json.\("enDesc").validate[String]
      } yield Degree(abbrev, deLabel, enLabel, deDesc, enDesc)

  implicit def languageReads: Reads[Language] =
    (json: JsValue) =>
      for {
        abbrev <- json.\("id").validate[String]
        deLabel <- json.\("deLabel").validate[String]
        enLabel <- json.\("enLabel").validate[String]
      } yield Language(abbrev, deLabel, enLabel)

  implicit def seasonReads: Reads[Season] =
    (json: JsValue) =>
      for {
        abbrev <- json.\("id").validate[String]
        deLabel <- json.\("deLabel").validate[String]
        enLabel <- json.\("enLabel").validate[String]
      } yield Season(abbrev, deLabel, enLabel)

  implicit def personReads: Reads[Identity] =
    (json: JsValue) =>
      for {
        id <- json.\("id").validate[String]
        kind <- json.\("kind").validate[String]
        person <- kind match {
          case Identity.PersonKind =>
            for {
              lastname <- json.\("lastname").validate[String]
              firstname <- json.\("firstname").validate[String]
              title <- json.\("title").validate[String]
              abbreviation <- json.\("abbreviation").validate[String]
              campusId <- json.\("campusId").validate[String]
            } yield Identity.Person(
              id,
              lastname,
              firstname,
              title,
              abbreviation,
              campusId
            )
          case Identity.GroupKind =>
            for {
              label <- json.\("label").validate[String]
            } yield Identity.Group(id, label)
          case Identity.UnknownKind =>
            for {
              label <- json.\("label").validate[String]
            } yield Identity.Unknown(id, label)
        }
      } yield person
}
