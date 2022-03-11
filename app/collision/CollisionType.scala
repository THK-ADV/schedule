package collision

import scala.util.{Failure, Success, Try}

sealed trait CollisionType {
  def label: String
  override def toString = label
}

object CollisionType {
  case object CourseRoom extends CollisionType {
    override val label = "Parallele Raumnutzung mehrerer Kurse"
  }
  case object StudyPathCourse extends CollisionType {
    override val label = "Kursüberschneidung im Studiengang"
  }
  case object CourseMultipleRoom extends CollisionType {
    override val label = "Raumüberschneidung im Studiengang"
  }
  case object LecturerCourse extends CollisionType {
    override val label = "Lehrender lehrt zeitgleich mehrere Kurse"
  }
  case object LecturerRoom extends CollisionType {
    override val label = "Lehrender ist zeitgleich in mehreren Räumen"
  }
  case object BlockedDay extends CollisionType {
    override val label = "Geblockter Tag"
  }

  def apply(label: String): Try[CollisionType] = label match {
    case "Parallele Raumnutzung mehrerer Kurse"        => Success(CourseRoom)
    case "Kursüberschneidung im Studiengang"           => Success(StudyPathCourse)
    case "Raumüberschneidung im Studiengang"           => Success(CourseMultipleRoom)
    case "Lehrender lehrt zeitgleich mehrere Kurse"    => Success(LecturerCourse)
    case "Lehrender ist zeitgleich in mehreren Räumen" => Success(LecturerRoom)
    case "Geblockter Tag"                              => Success(BlockedDay)
    case _                                             => Failure(new Throwable(s"unknown collision type $label"))
  }

  def all(): List[CollisionType] = List(
    CourseRoom,
    StudyPathCourse,
    CourseMultipleRoom,
    LecturerCourse,
    LecturerRoom,
    BlockedDay
  )
}
