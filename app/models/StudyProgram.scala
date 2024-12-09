package models

import java.util.UUID

import localization.LocalizedLabel
import play.api.libs.json.Json
import play.api.libs.json.Writes

case class StudyProgram(
    id: UUID,
    teachingUnit: UUID,
    degree: String,
    deLabel: String,
    enLabel: String,
    abbrev: String,
    poId: String,
    poNumber: Int,
    specializationId: Option[String]
) extends UniqueEntity[UUID]
    with LocalizedLabel

object StudyProgram {
  implicit def writes: Writes[StudyProgram] = Json.writes[StudyProgram]
}
