package models

import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class StudyProgram(
    teachingUnit: UUID,
    graduation: UUID,
    label: String,
    abbreviation: String,
    id: UUID
) extends UniqueEntity

object StudyProgram {
  implicit val format: OFormat[StudyProgram] = Json.format[StudyProgram]
}
