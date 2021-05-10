package models

import play.api.libs.json.{Json, OFormat}

case class GraduationJson(label: String, abbreviation: String)

object GraduationJson {
  implicit val format: OFormat[GraduationJson] = Json.format[GraduationJson]
}
