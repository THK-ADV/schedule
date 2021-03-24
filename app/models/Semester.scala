package models

import play.api.libs.json._

import java.util.UUID

case class Semester(name: String, id: UUID)

object Semester {
  implicit val format: OFormat[Semester] = Json.format[Semester]
}
