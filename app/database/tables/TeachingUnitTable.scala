package database.tables

import database.UniqueDbEntry
import database.cols._
import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp
import java.util.UUID

case class TeachingUnitDbEntry(
    faculty: UUID,
    label: String,
    abbreviation: String,
    number: Int,
    lastModified: Timestamp,
    id: UUID
) extends UniqueDbEntry

class TeachingUnitTable(tag: Tag)
    extends Table[TeachingUnitDbEntry](tag, "teaching_unit")
    with UniqueEntityColumn
    with AbbreviationColumn
    with LabelColumn
    with NumberColumn
    with FacultyColumn {

  def * = (
    faculty,
    label,
    abbreviation,
    number,
    lastModified,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: (
      (UUID, String, String, Int, Timestamp, UUID)
  ) => TeachingUnitDbEntry = {
    case (faculty, label, abbreviation, number, lastModified, id) =>
      TeachingUnitDbEntry(
        faculty,
        label,
        abbreviation,
        number,
        lastModified,
        id
      )
  }

  def unmapRow: TeachingUnitDbEntry => Option[
    (UUID, String, String, Int, Timestamp, UUID)
  ] =
    a =>
      Option(
        (a.faculty, a.label, a.abbreviation, a.number, a.lastModified, a.id)
      )
}
