package models

import localization.LocalizedLabel
import play.api.libs.json.{Json, Writes}

import java.util.UUID

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
    with LocalizedLabel {
  def isSamePO(po: String, specialization: Option[String]) = {
    val samePo = this.poId == po
    if this.specializationId.isDefined && specialization.isDefined
    then samePo && this.specializationId.get == specialization.get
    else samePo
  }
}

object StudyProgram {
  implicit def writes: Writes[StudyProgram] = Json.writes[StudyProgram]
}
