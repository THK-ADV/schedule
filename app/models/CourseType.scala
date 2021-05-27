package models

import play.api.libs.json._

sealed trait CourseType {
  override def toString = CourseType.unapply(this)
}

object CourseType {
  implicit val writes: Writes[CourseType] =
    Writes[CourseType](unapply _ andThen JsString)

  implicit val reads: Reads[CourseType] =
    Reads(_.validate[String].map(apply))

  def apply(string: String): CourseType = string match {
    case "lecture"   => Lecture
    case "practical" => Practical
    case "exercise"  => Exercise
    case "tutorial"  => Tutorial
    case "seminar"   => Seminar
    case _           => Unknown
  }

  def unapply(c: CourseType): String = c match {
    case Lecture   => "lecture"
    case Practical => "practical"
    case Exercise  => "exercise"
    case Tutorial  => "tutorial"
    case Seminar   => "seminar"
    case Unknown   => "unknown"
  }

  case object Lecture extends CourseType

  case object Practical extends CourseType

  case object Exercise extends CourseType

  case object Tutorial extends CourseType

  case object Seminar extends CourseType

  case object Unknown extends CourseType
}
