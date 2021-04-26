package database.tables

import database.cols.{AbbreviationColumn, IDColumn, LabelColumn}
import models.Faculty
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

class FacultyTable(tag: Tag)
    extends Table[Faculty](tag, "faculty")
    with IDColumn
    with AbbreviationColumn
    with LabelColumn {

  def * = (
    label,
    abbreviation,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: ((String, String, UUID)) => Faculty = {
    case (label, abbreviation, id) =>
      Faculty(label, abbreviation, id)
  }

  def unmapRow: Faculty => Option[(String, String, UUID)] = f =>
    Option((f.label, f.abbreviation, f.id))
}
