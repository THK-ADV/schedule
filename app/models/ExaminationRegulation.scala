package models

import database.SQLDateConverter
import database.tables.ExaminationRegulationDbEntry
import models.StudyProgram.StudyProgramAtom
import org.joda.time.LocalDate

import java.util.UUID

sealed trait ExaminationRegulation extends UniqueEntity {
  def studyProgramId: UUID

  def number: Int

  def start: LocalDate

  def end: Option[LocalDate]
}

object ExaminationRegulation extends SQLDateConverter {

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
}
