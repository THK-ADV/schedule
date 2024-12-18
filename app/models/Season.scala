package models

import localization.LocalizedLabel
import play.api.libs.json.Json
import play.api.libs.json.Writes

case class Season(id: String, deLabel: String, enLabel: String) extends UniqueEntity[String] with LocalizedLabel

object Season {
  implicit def writes: Writes[Season] = Json.writes[Season]
}
