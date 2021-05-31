package database.cols

import database.tables.ExaminationRegulationTable
import slick.jdbc.PostgresProfile.api._

import java.sql.Date
import java.util.UUID

trait ExaminationRegulationColumn {
  self: Table[_] =>
  def examinationRegulation = column[UUID]("examination_regulation")

  def examinationRegulationFk =
    foreignKey(
      "examinationRegulation",
      examinationRegulation,
      TableQuery[ExaminationRegulationTable]
    )(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )

  def examinationRegulation(id: UUID): Rep[Boolean] =
    examinationRegulation === id

  def examinationRegulationNumber(n: Int) =
    examinationRegulationFk.filter(_.hasNumber(n)).exists

  def examinationRegulationOnStart(date: Date) =
    examinationRegulationFk.filter(_.onStart(date)).exists

  def examinationRegulationSinceStart(date: Date) =
    examinationRegulationFk.filter(_.sinceStart(date)).exists

  def examinationRegulationOnEnd(date: Date) =
    examinationRegulationFk.filter(_.onEnd(date)).exists

  def examinationRegulationUntilEnd(date: Date) =
    examinationRegulationFk.filter(_.untilEnd(date)).exists

  def studyProgram(id: UUID) =
    examinationRegulationFk.filter(_.studyProgram(id)).exists

  def studyProgramLabel(label: String) =
    examinationRegulationFk.filter(_.studyProgramLabel(label)).exists

  def studyProgramAbbreviation(abbrev: String) =
    examinationRegulationFk.filter(_.studyProgramAbbreviation(abbrev)).exists

  def studyProgramGraduation(id: UUID) =
    examinationRegulationFk.filter(_.studyProgramGraduation(id)).exists

  def studyProgramTeachingUnit(id: UUID) =
    examinationRegulationFk.filter(_.studyProgramTeachingUnit(id)).exists
}
