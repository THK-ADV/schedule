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

  def apply(string: String): CourseInterval = string.toLowerCase match {
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

  case object Regularly extends CourseInterval // Unit

  case object Irregularly extends CourseInterval // List[Date]

  case object Block extends CourseInterval // List[Range[Date]]

  case object Unknown extends CourseInterval
}
