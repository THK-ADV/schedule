package models

import localization.LocalizedLabel
import play.api.libs.json.{Json, Writes}

case class Language(id: String, deLabel: String, enLabel: String)
    extends UniqueEntity[String]
    with LocalizedLabel

object Language {
  implicit def writes: Writes[Language] = Json.writes[Language]
}
