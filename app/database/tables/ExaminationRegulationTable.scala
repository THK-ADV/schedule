package database.tables

import database.UniqueDbEntry
import database.cols._
import slick.jdbc.PostgresProfile.api._

import java.sql.{Date, Timestamp}
import java.util.UUID

case class ExaminationRegulationDbEntry(
    studyProgram: UUID,
    number: Int,
    start: Date,
    end: Option[Date],
    lastModified: Timestamp,
    id: UUID
) extends UniqueDbEntry

class ExaminationRegulationTable(tag: Tag)
    extends Table[ExaminationRegulationDbEntry](tag, "examination_regulation")
    with UniqueEntityColumn
    with StudyProgramColumn
    with NumberColumn {

  def start = column[Date]("start")

  def end = column[Option[Date]]("end")

  def onStart(date: Date): Rep[Boolean] =
    start === date

  def sinceStart(date: Date): Rep[Boolean] =
    start >= date

  def untilEnd(date: Date): Rep[Boolean] =
    (end <= date) getOrElse false

  def onEnd(date: Date): Rep[Boolean] =
    (end === date) getOrElse false

  def * = (
    studyProgram,
    number,
    start,
    end,
    lastModified,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: (
      (UUID, Int, Date, Option[Date], Timestamp, UUID)
  ) => ExaminationRegulationDbEntry = {
    case (
          studyProgram,
          number,
          start,
          end,
          lastModified,
          id
        ) =>
      ExaminationRegulationDbEntry(
        studyProgram,
        number,
        start,
        end,
        lastModified,
        id
      )
  }

  def unmapRow: ExaminationRegulationDbEntry => Option[
    (UUID, Int, Date, Option[Date], Timestamp, UUID)
  ] = s =>
    Option(
      (
        s.studyProgram,
        s.number,
        s.start,
        s.end,
        s.lastModified,
        s.id
      )
    )
}
