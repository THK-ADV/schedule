package models

import play.api.libs.json._

sealed trait CourseInterval {
  override def toString = CourseInterval.unapply(this)
}

object CourseInterval {
  implicit val writes: Writes[CourseInterval] =
    Writes[CourseInterval](unapply _ andThen JsString)

  implicit val reads: Reads[CourseInterval] =
    Reads(_.validate[String].map(apply))

  def apply(string: String): CourseInterval = string match {
    case "regularly"   => Regularly
    case "irregularly" => Irregularly
    case "block"       => Block
    case _             => Unknown
  }

  def unapply(c: CourseInterval): String = c match {
    case Regularly   => "regularly"
    case Irregularly => "irregularly"
    case Block       => "block"
    case Unknown     => "unknown"
  }

  case object Regularly extends CourseInterval

  case object Irregularly extends CourseInterval

  case object Block extends CourseInterval

  case object Unknown extends CourseInterval
}
