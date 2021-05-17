package models

import play.api.libs.json.{Json, OFormat}

case class CampusJson(label: String, abbreviation: String)

object CampusJson {
  implicit val format: OFormat[CampusJson] = Json.format[CampusJson]
}
