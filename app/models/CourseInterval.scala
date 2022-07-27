package models

sealed trait CourseInterval {
  override def toString = CourseInterval.unapply(this)
}

object CourseInterval {

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

  case object Regularly extends CourseInterval

  case object Irregularly extends CourseInterval

  case object Block extends CourseInterval

  case object Unknown extends CourseInterval
}
