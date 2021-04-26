package models

import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class StudyProgramAssociation(
    teachingUnit: UUID,
    studyProgram: UUID,
    id: UUID
) extends UniqueEntity

object StudyProgramAssociation {
  implicit val format: OFormat[StudyProgramAssociation] =
    Json.format[StudyProgramAssociation]
}
