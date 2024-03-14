package database.tables

import database.UUIDUniqueColumn
import models.TeachingUnit
import slick.jdbc.PostgresProfile.api._

final class TeachingUnitTable(tag: Tag)
    extends Table[TeachingUnit](tag, "teaching_unit")
    with UUIDUniqueColumn {

  def deLabel = column[String]("de_label")

  def enLabel = column[String]("en_label")

  def faculty = column[String]("faculty")

  def * = (
    id,
    faculty,
    deLabel,
    enLabel
  ) <> ((TeachingUnit.apply _).tupled, TeachingUnit.unapply)
}
