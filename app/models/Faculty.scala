package models

import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class Faculty(label: String, abbreviation: String, number: Int, id: UUID)
    extends UniqueEntity

object Faculty {
  implicit val format: OFormat[Faculty] = Json.format[Faculty]
}
