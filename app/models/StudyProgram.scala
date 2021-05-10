package models

import play.api.libs.json.{Json, Writes}

import java.util.UUID

sealed trait StudyProgram extends UniqueEntity {
  def teachingUnitId: UUID

  def graduationId: UUID

  def label: String

  def abbreviation: String
}

object StudyProgram {
  implicit val writes: Writes[StudyProgram] = Writes.apply {
    case default: StudyProgramDefault => writesDefault.writes(default)
    case atom: StudyProgramAtom       => writesAtom.writes(atom)
  }

  implicit val writesDefault: Writes[StudyProgramDefault] =
    Json.writes[StudyProgramDefault]

  implicit val writesAtom: Writes[StudyProgramAtom] =
    Json.writes[StudyProgramAtom]

  case class StudyProgramDefault(
      teachingUnit: UUID,
      graduation: UUID,
      label: String,
      abbreviation: String,
      id: UUID
  ) extends StudyProgram {
    override def teachingUnitId = teachingUnit

    override def graduationId = graduation
  }

  case class StudyProgramAtom(
      teachingUnit: TeachingUnit,
      graduation: Graduation,
      label: String,
      abbreviation: String,
      id: UUID
  ) extends StudyProgram {
    override def teachingUnitId = teachingUnit.id

    override def graduationId = graduation.id
  }

}
