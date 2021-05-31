package models

import database.SQLDateConverter
import database.tables.{
  ExaminationRegulationDbEntry,
  GraduationDbEntry,
  StudyProgramDBEntry,
  TeachingUnitDbEntry
}
import date.LocalDateFormat
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

object ExaminationRegulation extends LocalDateFormat with SQLDateConverter {
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
      db.number,
      db.start,
      db.end.map(toLocalDate),
      db.id
    )

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

  object ExaminationRegulationAtom {
    def apply(
        e: ExaminationRegulationDbEntry,
        sp: StudyProgramDBEntry,
        tu: TeachingUnitDbEntry,
        g: GraduationDbEntry
    ): ExaminationRegulationAtom = ExaminationRegulationAtom(
      StudyProgramAtom(
        TeachingUnit(tu),
        Graduation(g),
        sp.label,
        sp.abbreviation,
        sp.id
      ),
      e.number,
      e.start,
      e.end.map(toLocalDate),
      e.id
    )
  }

}
