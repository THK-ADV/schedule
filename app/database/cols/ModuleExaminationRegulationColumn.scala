package database.cols

import database.tables.ModuleExaminationRegulationTable
import slick.jdbc.PostgresProfile.api._

import java.sql.Date
import java.util.UUID

trait ModuleExaminationRegulationColumn {
  self: Table[_] =>

  def moduleExaminationRegulation =
    column[UUID]("module_examination_regulation")

  def moduleExaminationRegulationFk =
    foreignKey(
      "moduleExaminationRegulation",
      moduleExaminationRegulation,
      TableQuery[ModuleExaminationRegulationTable]
    )(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )

  def moduleExaminationRegulation(id: UUID): Rep[Boolean] =
    moduleExaminationRegulation === id

  def module(id: UUID) =
    moduleExaminationRegulationFk.filter(_.isModule(id)).exists

  def examinationRegulation(id: UUID) =
    moduleExaminationRegulationFk.filter(_.examinationRegulation(id)).exists

  def isMandatory(mandatory: Boolean) =
    moduleExaminationRegulationFk.filter(_.isMandatory(mandatory)).exists

  def examinationRegulationNumber(n: Int) =
    moduleExaminationRegulationFk
      .filter(_.examinationRegulationNumber(n))
      .exists

  def examinationRegulationOnStart(date: Date) =
    moduleExaminationRegulationFk
      .filter(_.examinationRegulationOnStart(date))
      .exists

  def examinationRegulationSinceStart(date: Date) =
    moduleExaminationRegulationFk
      .filter(_.examinationRegulationSinceStart(date))
      .exists

  def examinationRegulationOnEnd(date: Date) =
    moduleExaminationRegulationFk
      .filter(_.examinationRegulationOnEnd(date))
      .exists

  def examinationRegulationUntilEnd(date: Date) =
    moduleExaminationRegulationFk
      .filter(_.examinationRegulationUntilEnd(date))
      .exists

  def studyProgram(id: UUID) =
    moduleExaminationRegulationFk.filter(_.studyProgram(id)).exists

  def studyProgramLabel(label: String) =
    moduleExaminationRegulationFk.filter(_.studyProgramLabel(label)).exists

  def studyProgramAbbreviation(abbrev: String) =
    moduleExaminationRegulationFk
      .filter(_.studyProgramAbbreviation(abbrev))
      .exists

  def studyProgramGraduation(id: UUID) =
    moduleExaminationRegulationFk.filter(_.studyProgramGraduation(id)).exists

  def studyProgramTeachingUnit(id: UUID) =
    moduleExaminationRegulationFk.filter(_.studyProgramTeachingUnit(id)).exists

}
