package database.tables

import database.StringUniqueColumn
import models.Faculty
import slick.jdbc.PostgresProfile.api._

class FacultyTable(tag: Tag)
    extends Table[Faculty](tag, "faculty")
    with StringUniqueColumn {

  def deLabel = column[String]("de_label")

  def enLabel = column[String]("en_label")

  def * = (
    id,
    deLabel,
    enLabel
  ) <> ((Faculty.apply _).tupled, Faculty.unapply)
}
