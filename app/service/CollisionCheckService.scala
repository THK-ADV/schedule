package service

import collision.Collision
import models.Schedule

import javax.inject.{Inject, Singleton}

@Singleton
final class CollisionCheckService @Inject() () {
  import collision.CollisionCheck._

  def checkForCollisions(
      schedules: Vector[Schedule],
      blockedDays: Set[Blocked]
  ): Vector[Collision] = {
    val binaryCollisions = List(courseRoomCollision, studyPathCourseCollision)
    val unaryCollisions = List(blockedCollision(blockedDays))
    eval(schedules, binaryCollisions, unaryCollisions)
  }
}
