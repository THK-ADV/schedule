package database.tables

import database.SQLDateConverter
import database.cols.{
  AbbreviationColumn,
  IDColumn,
  LabelColumn,
  StudyProgramColumn
}
import models.ExaminationRegulation
import org.joda.time.LocalDate
import slick.jdbc.PostgresProfile.api._

import java.sql.Date
import java.util.UUID

class ExaminationRegulationTable(tag: Tag)
    extends Table[ExaminationRegulation](tag, "examination_regulation")
    with IDColumn
    with SQLDateConverter
    with LabelColumn
    with AbbreviationColumn
    with StudyProgramColumn {

  def accreditation = column[Date]("accreditation_date")

  def activation = column[Date]("activation_date")

  def expiring = column[Date]("expiring_date")

  def isUniqueTo(
      studyProgram: UUID,
      accreditation: LocalDate,
      activation: LocalDate,
      expiring: LocalDate
  ) =
    hasStudyProgram(studyProgram) &&
      this.accreditation === toSQLDate(accreditation) &&
      this.activation === toSQLDate(activation) &&
      this.expiring === toSQLDate(expiring)

  def * = (
    studyProgram,
    label,
    abbreviation,
    accreditation,
    activation,
    expiring,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: (
      (UUID, String, String, Date, Date, Date, UUID)
  ) => ExaminationRegulation = {
    case (
          studyProgram,
          label,
          abbreviation,
          accreditation,
          activation,
          expiring,
          id
        ) =>
      ExaminationRegulation(
        studyProgram,
        label,
        abbreviation,
        accreditation,
        activation,
        expiring,
        id
      )
  }

  def unmapRow: ExaminationRegulation => Option[
    (UUID, String, String, Date, Date, Date, UUID)
  ] = s =>
    Option(
      (
        s.studyProgram,
        s.label,
        s.abbreviation,
        s.accreditation,
        s.activation,
        s.expiring,
        s.id
      )
    )
}
