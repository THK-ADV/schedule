package collision

import models.{Schedule, StudyProgram}
import org.joda.time.{Interval, LocalDate}

import java.util.UUID

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

  def studyPathPred(
      lower: Schedule => StudyProgram,
      studyProgram: UUID => StudyProgram
  ): BinarySchedulePredicate = (lhs, rhs) => {
    def go(lhs: StudyProgram, rhs: StudyProgram): Boolean =
      lhs.id == rhs.id || lhs.parentId.exists(p => go(studyProgram(p), rhs))
    go(lower(lhs), lower(rhs)) || go(lower(rhs), lower(lhs))
  }

  def coursePred: BinarySchedulePredicate = (lhs, rhs) =>
    lhs.courseId == rhs.courseId

  def lecturerPred(f: Schedule => UUID): BinarySchedulePredicate = (lhs, rhs) =>
    f(lhs) == f(rhs)

  // unary schedule predicates

  def blockedPred(blocked: Set[Blocked]): UnarySchedulePredicate = s =>
    blocked.contains(s.date)

  // predicate composition

  def combinePredicates(xs: BinarySchedulePredicate*): BinarySchedulePredicate =
    (lhs, rhs) => xs.forall(_.apply(lhs, rhs))

  // decoration

  def not(pred: BinarySchedulePredicate): BinarySchedulePredicate =
    (lhs, rhs) => !pred(lhs, rhs)

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

  def courseRoomCollision: BinaryCollisionCheck = binaryCollisionCheck(
    combinePredicates(datePred, roomPred, not(coursePred)),
    (a, b) => Collision(CollisionType.CourseRoom, a, b)
  )

  def studyPathCourseCollision(
      lower: Schedule => StudyProgram,
      studyProgram: UUID => StudyProgram
  ): BinaryCollisionCheck = binaryCollisionCheck(
    combinePredicates(
      datePred,
      studyPathPred(lower, studyProgram),
      not(coursePred)
    ),
    (a, b) => Collision(CollisionType.StudyPathCourse, a, b)
  )

  def courseMultipleRoomCollision: BinaryCollisionCheck = binaryCollisionCheck(
    combinePredicates(datePred, coursePred, not(roomPred)),
    (a, b) => Collision(CollisionType.CourseMultipleRoom, a, b)
  )

  def lecturerCourseCollision(f: Schedule => UUID): BinaryCollisionCheck =
    binaryCollisionCheck(
      combinePredicates(datePred, lecturerPred(f), not(coursePred)),
      (a, b) => Collision(CollisionType.LecturerCourse, a, b)
    )

  def lecturerRoomCollision(f: Schedule => UUID): BinaryCollisionCheck =
    binaryCollisionCheck(
      combinePredicates(datePred, lecturerPred(f), not(roomPred)),
      (a, b) => Collision(CollisionType.LecturerRoom, a, b)
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
