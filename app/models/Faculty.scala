package models

import localization.LocalizedLabel
import play.api.libs.json.{Json, Writes}

case class Faculty(id: String, deLabel: String, enLabel: String)
    extends UniqueEntity[String]
    with LocalizedLabel

object Faculty {
  implicit def writes: Writes[Faculty] = Json.writes[Faculty]
}
