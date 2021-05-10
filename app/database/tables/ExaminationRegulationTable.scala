package database.tables

import database.cols._
import database.{SQLDateConverter, UniqueDbEntry}
import slick.jdbc.PostgresProfile.api._

import java.sql.{Date, Timestamp}
import java.util.UUID

case class ExaminationRegulationDbEntry(
    studyProgram: UUID,
    label: String,
    abbreviation: String,
    start: Date,
    end: Date,
    lastModified: Timestamp,
    id: UUID
) extends UniqueDbEntry

class ExaminationRegulationTable(tag: Tag)
    extends Table[ExaminationRegulationDbEntry](tag, "examination_regulation")
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
    lastModified,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: (
      (UUID, String, String, Date, Date, Timestamp, UUID)
  ) => ExaminationRegulationDbEntry = {
    case (
          studyProgram,
          label,
          abbreviation,
          start,
          end,
          lastModified,
          id
        ) =>
      ExaminationRegulationDbEntry(
        studyProgram,
        label,
        abbreviation,
        start,
        end,
        lastModified,
        id
      )
  }

  def unmapRow: ExaminationRegulationDbEntry => Option[
    (UUID, String, String, Date, Date, Timestamp, UUID)
  ] = s =>
    Option(
      (
        s.studyProgram,
        s.label,
        s.abbreviation,
        s.start,
        s.end,
        s.lastModified,
        s.id
      )
    )
}
