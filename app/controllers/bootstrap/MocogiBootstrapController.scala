package controllers.bootstrap

import database.repos._
import database.repos.abstracts.Create
import models._
import play.api.libs.json.{JsValue, Json, Reads}
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
    gradeRepository: GradeRepository,
    studyProgramRepository: StudyProgramRepository,
    studyProgramRelationRepository: StudyProgramRelationRepository,
    languageRepository: LanguageRepository,
    seasonRepository: SeasonRepository,
    personRepository: PersonRepository,
    moduleRepository: ModuleRepository,
    moduleInStudyProgramRepository: ModuleInStudyProgramRepository,
    moduleSupervisorRepository: ModuleSupervisorRepository,
    moduleRelationRepository: ModuleRelationRepository,
    teachingUnitService: TeachingUnitService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc) {
  private val url = "http://lwivs49.gm.fh-koeln.de:9001"

  def createFaculties = Action.async { _ =>
    create("faculties", facultyRepository)
  }

  def createGrades = Action.async { _ =>
    create("grades", gradeRepository)
  }

  def createLanguages = Action.async { _ =>
    create("languages", languageRepository)
  }

  def createSeasons = Action.async { _ =>
    create("seasons", seasonRepository)
  }

  def createPeople = Action.async { _ =>
    create("persons", personRepository)
  }

  def createStudyPrograms = Action.async { _ =>
    def makeStudyPrograms(
        sps: List[StudyProgramMocogi],
        pos: List[POMocogi],
        specs: List[SpecializationMocogi],
        tus: Seq[TeachingUnit]
    ) = {
      val studyPrograms = ListBuffer[StudyProgram]()
      val relations = ListBuffer[StudyProgramRelation]()
      pos.foreach { po =>
        val sp = sps.find(_.abbrev == po.program).get
        studyPrograms += StudyProgram(
          po.abbrev,
          tuMapping(sp.abbrev, tus),
          sp.grade,
          sp.deLabel,
          sp.enLabel,
          sp.externalAbbreviation,
          po.version,
          po.date,
          po.dateTo
        )
      }
      specs.foreach { spec =>
        val po = pos.find(_.abbrev == spec.po).get
        val sp = sps.find(_.abbrev == po.program).get

        studyPrograms += StudyProgram(
          spec.abbrev,
          tuMapping(sp.abbrev, tus),
          sp.grade,
          s"${sp.deLabel} - ${spec.label}",
          s"${sp.enLabel} - ${spec.label}",
          sp.externalAbbreviation,
          po.version,
          po.date,
          po.dateTo
        )

        relations += StudyProgramRelation(po.abbrev, spec.abbrev)
      }
      (studyPrograms.toList, relations.toList)
    }

    for {
      sps <- ws
        .url(s"$url/studyPrograms")
        .get()
        .map(_.json.validate[List[StudyProgramMocogi]].get)
      pos <- ws
        .url(s"$url/pos")
        .get()
        .map(_.json.validate[List[POMocogi]].get)
      specs <- ws
        .url(s"$url/specializations")
        .get()
        .map(_.json.validate[List[SpecializationMocogi]].get)
      tus <- teachingUnitService.all(atomic = false)
      (studyPrograms, relations) = makeStudyPrograms(sps, pos, specs, tus)
      xs <- studyProgramRepository.createOrUpdateMany(studyPrograms)
      ys <- studyProgramRelationRepository.createOrUpdateMany(relations)
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
    def makeModules(ms: List[ModuleMocogi]) = {
      val modules = ListBuffer[Module]()
      val modulesInStudyProgram = ListBuffer[ModuleInStudyProgram]()
      val moduleSupervisor = ListBuffer[ModuleSupervisor]()
      val moduleRelations = ListBuffer[ModuleRelation]()

      ms.foreach { m =>
        modules += Module(
          m.id,
          m.title,
          m.abbrev,
          m.language,
          m.season,
          ModuleType(m.moduleType),
          m.status == "active",
          m.workload.collect {
            case (k, v)
                if (k == "lecture" | k == "seminar" | k == "practical" | k == "exercise") && v > 0 =>
              ModulePart(k)
          }.toList
        )

        m.po.mandatory.foreach { po =>
          modulesInStudyProgram += ModuleInStudyProgram(
            UUID.randomUUID(),
            m.id,
            po.specialization.getOrElse(po.po),
            mandatory = true,
            po.recommendedSemester
          )
        }

        m.po.optional.foreach { po =>
          modulesInStudyProgram += ModuleInStudyProgram(
            UUID.randomUUID(),
            m.id,
            po.specialization.getOrElse(po.po),
            mandatory = false,
            po.recommendedSemester
          )
        }

        m.moduleManagement.foreach { person =>
          moduleSupervisor += ModuleSupervisor(m.id, person)
        }

        m.moduleRelation.collect {
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
        .url(s"$url/metadata")
        .get()
        .map(_.json.validate[List[ModuleMocogi]].get)
      (modules, modulesInStudyPrograms, moduleSupervisor, moduleRelations) =
        makeModules(ms)
      xs <- moduleRepository.createOrUpdateMany(modules)
      _ <- moduleInStudyProgramRepository.deleteAll()
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
      elems = resp.json.validate[List[A]].get
      xs <- repo.createOrUpdateMany(elems)
    } yield {
      val (created, updated) = xs.partition(_.isDefined)
      Ok(Json.obj("createdMods" -> created.size, "updatedMods" -> updated.size))
    }

  implicit def facultyReads: Reads[Faculty] =
    (json: JsValue) =>
      for {
        abbrev <- json.\("abbrev").validate[String]
        deLabel <- json.\("deLabel").validate[String]
        enLabel <- json.\("enLabel").validate[String]
      } yield Faculty(abbrev, deLabel, enLabel)

  implicit def gradeReads: Reads[Grade] =
    (json: JsValue) =>
      for {
        abbrev <- json.\("abbrev").validate[String]
        deLabel <- json.\("deLabel").validate[String]
        enLabel <- json.\("enLabel").validate[String]
        deDesc <- json.\("deDesc").validate[String]
        enDesc <- json.\("enDesc").validate[String]
      } yield Grade(abbrev, deLabel, enLabel, deDesc, enDesc)

  implicit def languageReads: Reads[Language] =
    (json: JsValue) =>
      for {
        abbrev <- json.\("abbrev").validate[String]
        deLabel <- json.\("deLabel").validate[String]
        enLabel <- json.\("enLabel").validate[String]
      } yield Language(abbrev, deLabel, enLabel)

  implicit def seasonReads: Reads[Season] =
    (json: JsValue) =>
      for {
        abbrev <- json.\("abbrev").validate[String]
        deLabel <- json.\("deLabel").validate[String]
        enLabel <- json.\("enLabel").validate[String]
      } yield Season(abbrev, deLabel, enLabel)

  implicit def personReads: Reads[Person] =
    (json: JsValue) =>
      for {
        id <- json.\("id").validate[String]
        kind <- json.\("kind").validate[String]
        person <- kind match {
          case Person.DefaultKind =>
            for {
              lastname <- json.\("lastname").validate[String]
              firstname <- json.\("firstname").validate[String]
              title <- json
                .\("title")
                .validate[String]
                .map(s => Option.unless(s == "--")(s))
              abbreviation <- json.\("abbreviation").validate[String]
              campusId <- json.\("campusId").validate[String]
              status <- json.\("status").validate[String].map(_ == "active")
            } yield Person.Default(
              id,
              lastname,
              firstname,
              title,
              abbreviation,
              campusId,
              status
            )
          case Person.GroupKind =>
            for {
              label <- json.\("label").validate[String]
            } yield Person.Group(id, label)
          case Person.UnknownKind =>
            for {
              label <- json.\("label").validate[String]
            } yield Person.Unknown(id, label)
        }
      } yield person
}
