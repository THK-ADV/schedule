package models

import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class StudyProgramAssociationJson(teachingUnit: UUID, studyProgram: UUID)

object StudyProgramAssociationJson {
  implicit val format: OFormat[StudyProgramAssociationJson] =
    Json.format[StudyProgramAssociationJson]
}
