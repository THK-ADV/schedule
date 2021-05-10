package database.tables

import database.UniqueDbEntry
import database.cols._
import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp
import java.util.UUID

case class StudyProgramDBEntry(
    teachingUnit: UUID,
    graduation: UUID,
    label: String,
    abbreviation: String,
    lastModified: Timestamp,
    id: UUID
) extends UniqueDbEntry

class StudyProgramTable(tag: Tag)
    extends Table[StudyProgramDBEntry](tag, "study_program")
    with UniqueEntityColumn
    with AbbreviationColumn
    with LabelColumn
    with TeachingUnitColumn
    with GraduationColumn {

  def * = (
    teachingUnit,
    graduation,
    label,
    abbreviation,
    lastModified,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: (
      (UUID, UUID, String, String, Timestamp, UUID)
  ) => StudyProgramDBEntry = {
    case (teachingUnit, graduation, label, abbreviation, lastModified, id) =>
      StudyProgramDBEntry(
        teachingUnit,
        graduation,
        label,
        abbreviation,
        lastModified,
        id
      )
  }

  def unmapRow: StudyProgramDBEntry => Option[
    (UUID, UUID, String, String, Timestamp, UUID)
  ] =
    a =>
      Option(
        (
          a.teachingUnit,
          a.graduation,
          a.label,
          a.abbreviation,
          a.lastModified,
          a.id
        )
      )
}
