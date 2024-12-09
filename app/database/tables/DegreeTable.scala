package database.tables

import database.StringUniqueColumn
import models.Degree
import slick.jdbc.PostgresProfile.api._

final class DegreeTable(tag: Tag) extends Table[Degree](tag, "degree") with StringUniqueColumn {

  def deLabel = column[String]("de_label")

  def enLabel = column[String]("en_label")

  def deDesc = column[String]("de_desc")

  def enDesc = column[String]("en_desc")

  def * = (
    id,
    deLabel,
    enLabel,
    deDesc,
    enDesc
  ) <> (Degree.apply, Degree.unapply)
}
