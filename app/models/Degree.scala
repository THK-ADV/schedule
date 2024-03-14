package models

import localization.{LocalizedDescription, LocalizedLabel}
import play.api.libs.json.{Json, Writes}

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
