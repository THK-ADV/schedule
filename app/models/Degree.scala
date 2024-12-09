package models

import localization.LocalizedDescription
import localization.LocalizedLabel
import play.api.libs.json.Json
import play.api.libs.json.Writes

case class Degree(
    id: String,
    deLabel: String,
    enLabel: String,
    deDesc: String,
    enDesc: String
) extends UniqueEntity[String]
    with LocalizedLabel
    with LocalizedDescription

object Degree {
  implicit def writes: Writes[Degree] = Json.writes[Degree]
}
