package models

import localization.LocalizedLabel
import play.api.libs.json.Json
import play.api.libs.json.Writes

case class CourseType(id: CourseId, deLabel: String, enLabel: String) extends UniqueEntity[CourseId] with LocalizedLabel

object CourseType {
  implicit def writes: Writes[CourseType] = Json.writes
}
