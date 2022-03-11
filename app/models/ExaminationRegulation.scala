package models

import controllers.json.{JsonNullWritable, LocalDateFormat}
import database.SQLDateConverter
import database.tables.ExaminationRegulationDbEntry
import models.StudyProgram.StudyProgramAtom
import org.joda.time.LocalDate
import play.api.libs.json.{Json, Writes}

import java.util.UUID

sealed trait ExaminationRegulation extends UniqueEntity {
  def studyProgramId: UUID

  def number: Int

  def start: LocalDate

  def end: Option[LocalDate]
}

object ExaminationRegulation
    extends LocalDateFormat
    with SQLDateConverter
    with JsonNullWritable {
  implicit val writes: Writes[ExaminationRegulation] = Writes.apply {
    case default: ExaminationRegulationDefault => writesDefault.writes(default)
    case atom: ExaminationRegulationAtom       => writesAtom.writes(atom)
  }

  implicit val writesDefault: Writes[ExaminationRegulationDefault] =
    Json.writes[ExaminationRegulationDefault]

  implicit val writesAtom: Writes[ExaminationRegulationAtom] =
    Json.writes[ExaminationRegulationAtom]

  case class ExaminationRegulationDefault(
      studyProgram: UUID,
      number: Int,
      start: LocalDate,
      end: Option[LocalDate],
      id: UUID
  ) extends ExaminationRegulation {
    override def studyProgramId = studyProgram
  }

  case class ExaminationRegulationAtom(
      studyProgram: StudyProgramAtom,
      number: Int,
      start: LocalDate,
      end: Option[LocalDate],
      id: UUID
  ) extends ExaminationRegulation {
    override def studyProgramId = studyProgram.id
  }

  def apply(db: ExaminationRegulationDbEntry): ExaminationRegulationDefault =
    ExaminationRegulationDefault(
      db.studyProgram,
      db.number,
      db.start,
      db.end.map(toLocalDate),
      db.id
    )

  def apply(e: ExaminationRegulationDbEntry, sp: StudyProgramAtom) =
    ExaminationRegulationAtom(
      sp,
      e.number,
      e.start,
      e.end.map(toLocalDate),
      e.id
    )

}
