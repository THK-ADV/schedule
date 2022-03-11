package service

import collision.CollisionCheck._
import collision.{Collision, CollisionType}
import models.Schedule
import models.Schedule.ScheduleAtom

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CollisionService @Inject() (
    scheduleService: ScheduleService,
    implicit val ctx: ExecutionContext
) {

  def makeCollisionChecker(
      collisionTypes: Set[CollisionType],
      blocked: Set[Blocked],
      f: Schedule => UUID
  ): (List[BinaryCollisionCheck], List[UnaryCollisionCheck]) =
    collisionTypes.foldLeft(
      (List.empty[BinaryCollisionCheck], List.empty[UnaryCollisionCheck])
    ) { case ((bxs, uxs), ct) =>
      ct match {
        case CollisionType.CourseRoom =>
          (courseRoomCollision :: bxs, uxs)
        case CollisionType.StudyPathCourse =>
          (studyPathCourseCollision :: bxs, uxs)
        case CollisionType.CourseMultipleRoom =>
          (courseMultipleRoomCollision :: bxs, uxs)
        case CollisionType.LecturerCourse =>
          (lecturerCourseCollision(f) :: bxs, uxs)
        case CollisionType.LecturerRoom =>
          (lecturerRoomCollision(f) :: bxs, uxs)
        case CollisionType.BlockedDay =>
          (bxs, blockedCollision(blocked) :: uxs)
      }
    }

  def checkForCollisions(
      scheduleIds: List[UUID],
      collisionTypes: Set[CollisionType],
      blocked: Set[Blocked]
  ): Future[Vector[Collision]] = {
    val (bcls, ucls) = makeCollisionChecker(
      collisionTypes,
      blocked,
      _.asInstanceOf[ScheduleAtom].course.lecturerId
    )
    scheduleService
      .fromIds(scheduleIds, atomic = true)
      .map(xs => eval(xs.toVector, bcls, ucls))
  }
}
