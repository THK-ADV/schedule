package models

import play.api.libs.json.{Json, OFormat}

case class FacultyJson(label: String, abbreviation: String, number: Int)

object FacultyJson {
  implicit val format: OFormat[FacultyJson] = Json.format[FacultyJson]
}
