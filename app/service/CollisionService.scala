//package service
//
//import collision.CollisionCheck._
//import collision.{Collision, CollisionType}
//import models.Schedule.ScheduleAtom
//import models.StudyProgram.StudyProgramAtom
//import models.{Schedule, StudyProgram}
//
//import java.util.UUID
//import javax.inject.{Inject, Singleton}
//import scala.concurrent.{ExecutionContext, Future}
//
//@Singleton
//class CollisionService @Inject() (
//    private val scheduleService: ScheduleService,
//    implicit val ctx: ExecutionContext
//) {
//
//  private def makeCollisionChecker(
//      collisionTypes: Set[CollisionType],
//      blocked: Set[Blocked],
//      lecturer: Schedule => UUID,
//      studyProgram: Schedule => StudyProgram,
//      parent: StudyProgram => Option[StudyProgram]
//  ): (List[BinaryCollisionCheck], List[UnaryCollisionCheck]) =
//    collisionTypes.foldLeft(
//      (List.empty[BinaryCollisionCheck], List.empty[UnaryCollisionCheck])
//    ) { case ((bxs, uxs), ct) =>
//      ct match {
//        case CollisionType.CourseRoom =>
//          (courseRoomCollision :: bxs, uxs)
//        case CollisionType.StudyPathCourse =>
//          (studyPathCourseCollision(studyProgram, parent) :: bxs, uxs)
//        case CollisionType.CourseMultipleRoom =>
//          (courseMultipleRoomCollision :: bxs, uxs)
//        case CollisionType.LecturerCourse =>
//          (lecturerCourseCollision(lecturer) :: bxs, uxs)
//        case CollisionType.LecturerRoom =>
//          (lecturerRoomCollision(lecturer) :: bxs, uxs)
//        case CollisionType.BlockedDay =>
//          (bxs, blockedCollision(blocked) :: uxs)
//      }
//    }
//
//  def checkForCollisions(
//      scheduleIds: List[UUID],
//      collisionTypes: Set[CollisionType],
//      blocked: Set[Blocked]
//  ): Future[Vector[Collision]] = {
//    val (bcls, ucls) = makeCollisionChecker(
//      collisionTypes,
//      blocked,
//      _.asInstanceOf[ScheduleAtom].course.lecturerId,
//      _.asInstanceOf[
//        ScheduleAtom
//      ].moduleExaminationRegulation.examinationRegulation.studyProgram,
//      _.asInstanceOf[
//        StudyProgramAtom
//      ].parent
//    )
//    scheduleService
//      .fromIds(scheduleIds, atomic = true)
//      .map(xs => eval(xs.toVector, bcls, ucls))
//  }
//}
