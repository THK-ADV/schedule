package kafka

import database.repos.*
import models.{
  CourseId,
  Degree,
  Faculty,
  Module,
  ModuleInStudyProgram,
  ModuleSupervisor,
  StudyProgram
}
import org.apache.pekko.actor.Actor.Receive
import org.apache.pekko.actor.{Actor, ActorRef, ActorSystem, Props}
import play.api.Logging
import play.api.libs.functional.syntax.*
import play.api.libs.json.*

import java.util.UUID
import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

// TODO refactor

extension [A](result: JsResult[A])
  def toFuture: Future[A] =
    result match
      case JsSuccess(value, _) => Future.successful(value)
      case JsError(errors) => Future.failed(new Exception(errors.mkString(",")))

extension (self: StringID) {
  def isModule = self.id == "module"
  def isActive = self.id == "active"
}

trait DatabaseActor:
  protected def actorRef: ActorRef

  def createOrUpdateModule(js: JsValue): Unit =
    actorRef ! CreateOrUpdateModule(js)

  def createOrUpdateFaculty(js: JsValue): Unit =
    actorRef ! CreateOrUpdateFaculty(js)

  def createOrUpdateDegree(js: JsValue): Unit =
    actorRef ! CreateOrUpdateDegree(js)

object DatabaseActor:
  def apply(
      moduleRepo: ModuleRepository,
      moduleRelationRepo: ModuleRelationRepository,
      moduleSupervisorRepo: ModuleSupervisorRepository,
      moduleInStudyProgramRepo: ModuleInStudyProgramRepository,
      facultyRepo: FacultyRepository,
      degreeRepo: DegreeRepository,
      studyProgramRepository: StudyProgramRepository,
      system: ActorSystem,
      ctx: ExecutionContext
  ) =
    new DatabaseActor {
      override protected val actorRef: ActorRef =
        system.actorOf(
          Props(
            Impl(
              moduleRepo,
              moduleRelationRepo,
              moduleSupervisorRepo,
              moduleInStudyProgramRepo,
              facultyRepo,
              degreeRepo,
              studyProgramRepository
            )(using ctx)
          )
        )
    }

private case class CreateOrUpdateModule(js: JsValue) extends AnyVal

private case class CreateOrUpdateFaculty(js: JsValue) extends AnyVal

private case class CreateOrUpdateDegree(js: JsValue) extends AnyVal

private case class StringID(id: String) extends AnyVal

private case class UID(id: UUID) extends AnyVal

private case class POMandatory(
    id: StringID,
    specialization: Option[StringID],
    recommendedSemester: List[Int]
) {
  def isSamePO(po: String, specialization: Option[String]) = {
    val samePo = this.id.id == po
    if this.specialization.isDefined && specialization.isDefined
    then samePo && this.specialization.get.id == specialization.get
    else samePo
  }
}

private case class POOptional(
    id: StringID,
    specialization: Option[StringID],
    instanceOf: UID,
    partOfCatalog: Boolean,
    focus: Boolean,
    recommendedSemester: List[Int]
) {
  def isSamePO(po: String, specialization: Option[String]) = {
    val samePo = this.id.id == po
    if this.specialization.isDefined && specialization.isDefined
    then samePo && this.specialization.get.id == specialization.get
    else samePo
  }
}

private enum ModuleRelation:
  case Parent(children: List[UID]) extends ModuleRelation
  case Child(parent: UID) extends ModuleRelation

private class Impl(
    moduleRepo: ModuleRepository,
    moduleRelationRepo: ModuleRelationRepository,
    moduleSupervisorRepo: ModuleSupervisorRepository,
    moduleInStudyProgramRepo: ModuleInStudyProgramRepository,
    facultyRepo: FacultyRepository,
    degreeRepo: DegreeRepository,
    studyProgramRepository: StudyProgramRepository
)(using ctx: ExecutionContext)
    extends Actor
    with Logging:

  given Reads[StringID] = (JsPath \ "id").read[String].map(StringID.apply)

  given Reads[UID] = (JsPath \ "id").read[UUID].map(UID.apply)

  given Reads[POMandatory] = {
    val builder =
      ((JsPath \ "po").read[StringID] and
        (JsPath \ "specialization").readNullable[StringID] and
        (JsPath \ "recommendedSemester")
          .readNullable[List[Int]]
          .map(_.getOrElse(Nil)))
    builder(POMandatory.apply)
  }

  given Reads[POOptional] = {
    val builder =
      ((JsPath \ "po").read[StringID] and
        (JsPath \ "specialization").readNullable[StringID] and
        (JsPath \ "instanceOf").read[UID] and
        (JsPath \ "partOfCatalog").read[Boolean] and
        (JsPath \ "focus")
          .readNullable[Boolean]
          .map(_.getOrElse(false)) and
        (JsPath \ "recommendedSemester")
          .readNullable[List[Int]]
          .map(_.getOrElse(Nil)))
    builder(POOptional.apply)
  }

  given Reads[ModuleRelation] = {
    val child = (JsPath \ "parent").read[UID].map(ModuleRelation.Child.apply)
    val parent =
      (JsPath \ "children").read[List[UID]].map(ModuleRelation.Parent.apply)
    child or parent
  }

  type IsModule = Boolean

  type IsActive = Boolean

  type ModuleJson =
    (
        Module,
        IsModule,
        IsActive,
        Option[ModuleRelation],
        List[StringID],
        List[POMandatory],
        List[POOptional]
    )

  // TODO ModuleInStudyProgram Semester Dependency
  given Reads[ModuleJson] = {
    val root = JsPath \ "metadata"
    def courseIds = {
      val workload = root \ "workload"
      ((workload \ "lecture").read[Int] and
        (workload \ "seminar").read[Int] and
        (workload \ "practical").read[Int] and
        (workload \ "exercise").read[Int]).apply { (l, s, p, e) =>
        val b = ListBuffer.empty[CourseId]
        if l > 0 then b += CourseId.Lecture
        if s > 0 then b += CourseId.Seminar
        if p > 0 then b += CourseId.Practical
        if e > 0 then b += CourseId.Exercise
        b.toList
      }
    }
    val po = root \ "pos"
    val moduleRelation = (root \ "relation").readNullable[ModuleRelation]
    val module = ((root \ "id").read[UUID] and
      (root \ "title").read[String] and
      (root \ "abbrev").read[String] and
      (root \ "kind").read[StringID] and
      (root \ "language").read[StringID] and
      (root \ "season").read[StringID] and
      (root \ "status").read[StringID] and
      courseIds).apply(
      (id, title, abbrev, kind, language, season, status, courseIds) =>
        (
          Module(id, title, abbrev, language.id, season.id, courseIds),
          kind.isModule,
          status.isActive
        )
    )

    val builder = module and
      moduleRelation and
      (root \ "responsibilities" \ "moduleManagement").read[List[StringID]] and
      (po \ "mandatory")
        .readNullable[List[POMandatory]]
        .map(_.getOrElse(Nil)) and
      (po \ "optional")
        .readNullable[List[POOptional]]
        .map(_.getOrElse(Nil))
    builder.apply { (m, rel, mana, man, opt) =>
      (m._1, m._2, m._3, rel, mana, man, opt)
    }
  }

  private def createOrUpdatePORelation(
      module: UUID,
      poMandatory: List[POMandatory],
      poOptional: List[POOptional]
  ) = {
    def makeUpdates(
        existing: Seq[(ModuleInStudyProgram, StudyProgram)],
        studyPrograms: Seq[StudyProgram]
    ): List[ModuleInStudyProgram] = {
      val visitedMandatory = ListBuffer.empty[POMandatory]
      val visitedOptional = ListBuffer.empty[POOptional]
      val updates = existing.map { (msp, sp) =>
        poMandatory
          .find(_.isSamePO(sp.poId, sp.specializationId))
          .map { po =>
            visitedMandatory += po
            msp.copy(mandatory = true, focus = false, active = true)
          }
          .orElse(
            poOptional
              .find(_.isSamePO(sp.poId, sp.specializationId))
              .map { po =>
                visitedOptional += po
                msp.copy(mandatory = false, focus = po.focus, active = true)
              }
          )
          .getOrElse(msp.copy(active = false))
      }
      val newEntries = ListBuffer.empty[ModuleInStudyProgram]

      poMandatory
        .diff(visitedMandatory)
        .foreach { po =>
          studyPrograms.find(
            _.isSamePO(po.id.id, po.specialization.map(_.id))
          ) match
            case Some(sp) =>
              newEntries += ModuleInStudyProgram(
                UUID.randomUUID(),
                module,
                sp.id,
                mandatory = true,
                focus = false,
                po.recommendedSemester,
                active = true
              )
            case None =>
              logger.error(
                s"unable to find study program for: po ${po.id.id}, specialization: ${po.specialization.map(_.id)}"
              )
        }

      poOptional
        .diff(visitedOptional)
        .foreach { po =>
          studyPrograms.find(
            _.isSamePO(po.id.id, po.specialization.map(_.id))
          ) match
            case Some(sp) =>
              newEntries += ModuleInStudyProgram(
                UUID.randomUUID(),
                module,
                sp.id,
                mandatory = false,
                focus = po.focus,
                po.recommendedSemester,
                active = true
              )
            case None =>
              logger.error(
                s"unable to find study program for: po ${po.id.id}, specialization: ${po.specialization.map(_.id)}"
              )
        }
      updates.toList.appendedAll(newEntries.toList)
    }

    for
      studyPrograms <- studyProgramRepository.all()
      existingStudyProgram <- moduleInStudyProgramRepo.allFromModule(module)
      _ <- moduleInStudyProgramRepo.createOrUpdateMany(
        makeUpdates(existingStudyProgram, studyPrograms)
      )
    yield ()
  }

  private def createOrUpdateParentRelations(
      module: UUID,
      relation: Option[ModuleRelation]
  ) = {
    // child relations are implicitly set by the parent relation
    val relations = relation
      .collect { case ModuleRelation.Parent(children) =>
        children.map(c => models.ModuleRelation(module, c.id))
      }
      .getOrElse(Nil)
    moduleRelationRepo.createOrUpdateMany(module, relations)
  }

  private def createOrUpdateSupervisor(
      module: UUID,
      management: List[StringID]
  ) = {
    val supervisors = management.map(id => ModuleSupervisor(module, id.id))
    moduleSupervisorRepo.createOrUpdateMany(supervisors)
  }

  def createOrUpdateModule(
      module: Module,
      management: List[StringID],
      relation: Option[ModuleRelation],
      poMandatory: List[POMandatory],
      poOptional: List[POOptional]
  ): Future[Unit] =
    for
      _ <- moduleRepo.createOrUpdate(module)
      _ <- createOrUpdateSupervisor(module.id, management)
      _ <- createOrUpdateParentRelations(module.id, relation)
      _ <- createOrUpdatePORelation(module.id, poMandatory, poOptional)
    yield ()

  override def receive: Receive = {
    case CreateOrUpdateModule(js) =>
      js.validate[ModuleJson] match
        case JsSuccess(
              (
                module,
                isModule,
                isActive,
                relation,
                management,
                poMandatory,
                poOptional
              ),
              _
            ) =>
          if isModule && isActive then
            createOrUpdateModule(
              module,
              management,
              relation,
              poMandatory,
              poOptional
            ) onComplete {
              case Success(_) =>
                logger.info(
                  s"Module ${module.id} (${module.label}) created or updated"
                )
              case Failure(e) =>
                logger.error(
                  s"Failed to create or update module ${module.id} (${module.label})",
                  e
                )
            }
          else
            logger.info(
              s"Module ${module.id} (${module.label}) is not a normal module or inactive"
            )
        case JsError(errors) =>
          logger.error(s"Failed to parse module: $errors")
    case CreateOrUpdateFaculty(js) =>
      js.validate[Faculty]
        .toFuture
        .flatMap(facultyRepo.createOrUpdate)
        .onComplete(log(js))
    case CreateOrUpdateDegree(js) =>
      js.validate[Degree]
        .toFuture
        .flatMap(degreeRepo.createOrUpdate)
        .onComplete(log(js))
  }

  private def log[A](js: JsValue)(result: Try[Option[A]]): Unit =
    result match
      case Success(created) =>
        val action = if created.isDefined then "created" else "updated"
        logger.info(s"Successfully $action: $js")
      case Failure(e) => logger.error("Failed to create or update", e)
