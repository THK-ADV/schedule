package service

import collision.CollisionCheck._
import collision.{Collision, CollisionType}
import models.Schedule.ScheduleAtom
import models.{Schedule, StudyProgram}

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class CollisionService @Inject() (
    scheduleService: ScheduleService,
    studyProgramService: StudyProgramService,
    implicit val ctx: ExecutionContext
) {

  def makeCollisionChecker(
      collisionTypes: Set[CollisionType],
      blocked: Set[Blocked],
      lecturer: Schedule => UUID,
      lower: Schedule => StudyProgram,
      studyProgram: UUID => StudyProgram
  ): (List[BinaryCollisionCheck], List[UnaryCollisionCheck]) =
    collisionTypes.foldLeft(
      (List.empty[BinaryCollisionCheck], List.empty[UnaryCollisionCheck])
    ) { case ((bxs, uxs), ct) =>
      ct match {
        case CollisionType.CourseRoom =>
          (courseRoomCollision :: bxs, uxs)
        case CollisionType.StudyPathCourse =>
          (studyPathCourseCollision(lower, studyProgram) :: bxs, uxs)
        case CollisionType.CourseMultipleRoom =>
          (courseMultipleRoomCollision :: bxs, uxs)
        case CollisionType.LecturerCourse =>
          (lecturerCourseCollision(lecturer) :: bxs, uxs)
        case CollisionType.LecturerRoom =>
          (lecturerRoomCollision(lecturer) :: bxs, uxs)
        case CollisionType.BlockedDay =>
          (bxs, blockedCollision(blocked) :: uxs)
      }
    }

  private def studyProgramById(sp: UUID): Future[StudyProgram] =
    studyProgramService.get(sp, atomic = false).map(_.get)

  def checkForCollisions(
      scheduleIds: List[UUID],
      collisionTypes: Set[CollisionType],
      blocked: Set[Blocked]
  ): Future[Vector[Collision]] = {
    val (bcls, ucls) = makeCollisionChecker(
      collisionTypes,
      blocked,
      _.asInstanceOf[ScheduleAtom].course.lecturerId,
      _.asInstanceOf[
        ScheduleAtom
      ].moduleExaminationRegulation.examinationRegulation.studyProgram,
      s => Await.result(studyProgramById(s), Duration.Inf)
    )
    scheduleService
      .fromIds(scheduleIds, atomic = true)
      .map(xs => eval(xs.toVector, bcls, ucls))
  }
}
