package database.tables

import database.SQLDateConverter
import database.cols._
import models.ExaminationRegulation
import slick.jdbc.PostgresProfile.api._

import java.sql.Date
import java.util.UUID

class ExaminationRegulationTable(tag: Tag)
    extends Table[ExaminationRegulation](tag, "examination_regulation")
    with UniqueEntityColumn
    with SQLDateConverter
    with LabelColumn
    with AbbreviationColumn
    with StudyProgramColumn
    with StartEndColumn {

  def * = (
    studyProgram,
    label,
    abbreviation,
    start,
    end,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: (
      (UUID, String, String, Date, Date, UUID)
  ) => ExaminationRegulation = {
    case (
          studyProgram,
          label,
          abbreviation,
          start,
          end,
          id
        ) =>
      ExaminationRegulation(
        studyProgram,
        label,
        abbreviation,
        start,
        end,
        id
      )
  }

  def unmapRow: ExaminationRegulation => Option[
    (UUID, String, String, Date, Date, UUID)
  ] = s =>
    Option(
      (
        s.studyProgram,
        s.label,
        s.abbreviation,
        s.start,
        s.end,
        s.id
      )
    )
}
