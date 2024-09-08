package models

import localization.LocalizedLabel
import play.api.libs.json.{Json, Format}

case class Faculty(id: String, deLabel: String, enLabel: String)
    extends UniqueEntity[String]
    with LocalizedLabel

object Faculty {
  given Format[Faculty] = Json.format
}
