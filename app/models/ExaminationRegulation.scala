package models

import database.tables.ExaminationRegulationDbEntry
import date.LocalDateFormat
import org.joda.time.LocalDate
import play.api.libs.json.{Json, Writes}

import java.util.UUID

sealed trait ExaminationRegulation extends UniqueEntity {
  def studyProgramId: UUID

  def label: String

  def abbreviation: String

  def start: LocalDate

  def end: LocalDate
}

object ExaminationRegulation extends LocalDateFormat {
  implicit val writes: Writes[ExaminationRegulation] = Writes.apply {
    case default: ExaminationRegulationDefault => writesDefault.writes(default)
    case atom: ExaminationRegulationAtom       => writesAtom.writes(atom)
  }

  implicit val writesDefault: Writes[ExaminationRegulationDefault] =
    Json.writes[ExaminationRegulationDefault]

  implicit val writesAtom: Writes[ExaminationRegulationAtom] =
    Json.writes[ExaminationRegulationAtom]

  def apply(db: ExaminationRegulationDbEntry): ExaminationRegulationDefault =
    ExaminationRegulationDefault(
      db.studyProgram,
      db.label,
      db.abbreviation,
      db.start,
      db.end,
      db.id
    )

  case class ExaminationRegulationDefault(
      studyProgram: UUID,
      label: String,
      abbreviation: String,
      start: LocalDate,
      end: LocalDate,
      id: UUID
  ) extends ExaminationRegulation {
    override def studyProgramId = studyProgram
  }

  case class ExaminationRegulationAtom(
      studyProgram: StudyProgram,
      label: String,
      abbreviation: String,
      start: LocalDate,
      end: LocalDate,
      id: UUID
  ) extends ExaminationRegulation {
    override def studyProgramId = studyProgram.id
  }

}
