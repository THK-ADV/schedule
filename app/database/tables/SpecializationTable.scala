package database.tables

import database.StringUniqueColumn
import models.Specialization
import slick.jdbc.PostgresProfile.api._

final class SpecializationTable(tag: Tag) extends Table[Specialization](tag, "specialization") with StringUniqueColumn {

  def label = column[String]("label")

  def * = (
    id,
    label
  ) <> (Specialization.apply, Specialization.unapply)
}
