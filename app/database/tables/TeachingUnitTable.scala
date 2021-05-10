package database.tables

import database.cols.{AbbreviationColumn, UniqueEntityColumn, LabelColumn}
import models.TeachingUnit
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

class TeachingUnitTable(tag: Tag)
    extends Table[TeachingUnit](tag, "teaching_unit")
    with UniqueEntityColumn
    with AbbreviationColumn
    with LabelColumn {

  def number = column[Int]("number")

  def hasNumber(n: Int) = number === n

  def * = (
    label,
    abbreviation,
    number,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: ((String, String, Int, UUID)) => TeachingUnit = {
    case (label, abbreviation, number, id) =>
      TeachingUnit(label, abbreviation, number, id)
  }

  def unmapRow: TeachingUnit => Option[(String, String, Int, UUID)] = a =>
    Option((a.label, a.abbreviation, a.number, a.id))
}
