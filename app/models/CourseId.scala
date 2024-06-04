package models

import play.api.libs.json.Writes

sealed trait CourseId {
  def id: String
  override def toString = id
}

object CourseId {
  implicit def writes: Writes[CourseId] =
    Writes.of[String].contramap(_.id)

  case object Lecture extends CourseId {
    override def id: String = "lecture"
  }
  case object Seminar extends CourseId {
    override def id: String = "seminar"
  }
  case object Practical extends CourseId {
    override def id: String = "practical"
  }
  case object Exercise extends CourseId {
    override def id: String = "exercise"
  }
  case object Tutorial extends CourseId {
    override def id: String = "tutorial"
  }

  def apply(id: String): CourseId =
    id match {
      case "lecture"   => Lecture
      case "seminar"   => Seminar
      case "practical" => Practical
      case "exercise"  => Exercise
      case "tutorial"  => Tutorial
    }
}
