package database.tables

import database.cols.{AbbreviationColumn, IDColumn, LabelColumn}
import models.Graduation
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

class GraduationTable(tag: Tag)
    extends Table[Graduation](tag, "graduation")
    with IDColumn
    with AbbreviationColumn
    with LabelColumn {

  def * = (
    label,
    abbreviation,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: ((String, String, UUID)) => Graduation = {
    case (label, abbreviation, id) =>
      Graduation(label, abbreviation, id)
  }

  def unmapRow: Graduation => Option[(String, String, UUID)] = a =>
    Option((a.label, a.abbreviation, a.id))
}
