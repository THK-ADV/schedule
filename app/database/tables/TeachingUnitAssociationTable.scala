package database.tables

import database.cols.IDColumn
import models.TeachingUnitAssociation
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

class TeachingUnitAssociationTable(tag: Tag)
    extends Table[TeachingUnitAssociation](tag, "teaching_unit_association")
    with IDColumn {

  def faculty = column[UUID]("faculty")

  def teachingUnit = column[UUID]("teaching_unit")

  def * = (
    faculty,
    teachingUnit,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: ((UUID, UUID, UUID)) => TeachingUnitAssociation = {
    case (faculty, teachingUnit, id) =>
      TeachingUnitAssociation(faculty, teachingUnit, id)
  }

  def unmapRow: TeachingUnitAssociation => Option[(UUID, UUID, UUID)] = a =>
    Option((a.faculty, a.teachingUnit, a.id))
}
