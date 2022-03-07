package collision

import models.Schedule
import org.joda.time.{Interval, LocalDate}

object CollisionCheck {
  type BinarySchedulePredicate = (Schedule, Schedule) => Boolean
  type UnarySchedulePredicate = Schedule => Boolean
  type BinaryCollisionCheck = (Schedule, Schedule) => Option[Collision]
  type UnaryCollisionCheck = Schedule => Option[Collision]
  type Blocked = LocalDate

  // binary schedule predicates

  def datePred: BinarySchedulePredicate = (lhs, rhs) => {
    val left =
      new Interval(lhs.date.toDateTime(lhs.start), lhs.date.toDateTime(lhs.end))
    val right =
      new Interval(rhs.date.toDateTime(rhs.start), rhs.date.toDateTime(rhs.end))
    left overlaps right
  }

  def roomPred: BinarySchedulePredicate = (lhs, rhs) => lhs.roomId == rhs.roomId

  def studyPathPred: BinarySchedulePredicate = (lhs, rhs) =>
    lhs.moduleExaminationRegulationId == rhs.moduleExaminationRegulationId

  def coursePred: BinarySchedulePredicate = (lhs, rhs) =>
    lhs.courseId != rhs.courseId

  // unary schedule predicates

  def blockedPred(blocked: Set[Blocked]): UnarySchedulePredicate = s =>
    blocked.contains(s.date)

  // predicate composition

  def combinePredicates(xs: BinarySchedulePredicate*): BinarySchedulePredicate =
    (lhs, rhs) => xs.forall(_.apply(lhs, rhs))

  // collision check builder

  def binaryCollisionCheck(
      pred: BinarySchedulePredicate,
      makeCollision: (Schedule, Schedule) => Collision
  ): BinaryCollisionCheck = (lhs, rhs) =>
    Option.when(pred(lhs, rhs))(makeCollision(lhs, rhs))

  def unaryCollisionCheck(
      pred: UnarySchedulePredicate,
      makeCollision: Schedule => Collision
  ): UnaryCollisionCheck = s => Option.when(pred(s))(makeCollision(s))

  // schedule collision checks

  def roomCollision: BinaryCollisionCheck = binaryCollisionCheck(
    combinePredicates(datePred, roomPred, coursePred),
    (a, b) => Collision(CollisionType.Room, a, b)
  )

  def studyPathCollision: BinaryCollisionCheck = binaryCollisionCheck(
    combinePredicates(datePred, studyPathPred, coursePred),
    (a, b) => Collision(CollisionType.StudyPathTime, a, b)
  )

  def blockedCollision(blocked: Set[LocalDate]): UnaryCollisionCheck =
    unaryCollisionCheck(
      blockedPred(blocked),
      s => Collision(CollisionType.BlockedDay, s, s)
    )

  // collision check application

  def binaryEval(
      xs: Vector[Schedule],
      collisionChecks: List[BinaryCollisionCheck]
  ): Vector[Collision] =
    (
      for {
        (x, i) <- xs.zipWithIndex
        y <- xs.drop(i + 1)
        c <- collisionChecks.map(_.apply(x, y))
      } yield c
    ).flatten

  def unaryEval(
      xs: Vector[Schedule],
      collisionChecks: List[UnaryCollisionCheck]
  ): Vector[Collision] =
    (
      for {
        x <- xs
        c <- collisionChecks.map(_.apply(x))
      } yield c
    ).flatten

  def eval(
      xs: Vector[Schedule],
      binaryCollisionChecks: List[BinaryCollisionCheck],
      unaryCollisionChecks: List[UnaryCollisionCheck]
  ): Vector[Collision] = {
    val binaryRes = binaryEval(xs, binaryCollisionChecks)
    val unaryRes = unaryEval(xs, unaryCollisionChecks)
    binaryRes ++ unaryRes
  }
}
