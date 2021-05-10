package database.tables

import database.UniqueDbEntry
import database.cols.UniqueEntityColumn
import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp
import java.util.UUID

case class TeachingUnitAssociationDbEntry(
    faculty: UUID,
    teachingUnit: UUID,
    lastModified: Timestamp,
    id: UUID
) extends UniqueDbEntry

class TeachingUnitAssociationTable(tag: Tag)
    extends Table[TeachingUnitAssociationDbEntry](
      tag,
      "teaching_unit_association"
    )
    with UniqueEntityColumn {

  def faculty = column[UUID]("faculty")

  def teachingUnit = column[UUID]("teaching_unit")

  def * = (
    faculty,
    teachingUnit,
    lastModified,
    id
  ) <> (mapRow, unmapRow)

  def mapRow
      : ((UUID, UUID, Timestamp, UUID)) => TeachingUnitAssociationDbEntry = {
    case (faculty, teachingUnit, lastModified, id) =>
      TeachingUnitAssociationDbEntry(faculty, teachingUnit, lastModified, id)
  }

  def unmapRow: TeachingUnitAssociationDbEntry => Option[
    (UUID, UUID, Timestamp, UUID)
  ] = a => Option((a.faculty, a.teachingUnit, a.lastModified, a.id))
}
