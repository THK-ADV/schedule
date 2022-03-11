package models

import play.api.libs.json._

sealed trait ScheduleEntryStatus {
  override def toString = ScheduleEntryStatus.unapply(this)
}

object ScheduleEntryStatus {
  implicit val format: Format[ScheduleEntryStatus] =
    Format
      .of[String]
      .bimap(ScheduleEntryStatus.apply, ScheduleEntryStatus.unapply)

  def apply(string: String): ScheduleEntryStatus = string.toLowerCase match {
    case "active"           => Active
    case "draft"            => Draft
    case "pending_lecturer" => PendingLecturer
    case "pending_manager"  => PendingManager
    case _                  => Unknown
  }

  def unapply(lang: ScheduleEntryStatus): String = lang match {
    case Active          => "active"
    case Draft           => "draft"
    case PendingLecturer => "pending_lecturer"
    case PendingManager  => "pending_manager"
    case Unknown         => "unknown"
  }

  case object Active extends ScheduleEntryStatus
  case object Draft extends ScheduleEntryStatus
  case object PendingLecturer extends ScheduleEntryStatus
  case object PendingManager extends ScheduleEntryStatus
  case object Unknown extends ScheduleEntryStatus

}
