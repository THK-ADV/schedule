package database.tables

import database.cols.{LocalizedLabelColumn, StringUniqueColumn}
import models.Faculty
import slick.jdbc.PostgresProfile.api._

class FacultyTable(tag: Tag)
    extends Table[Faculty](tag, "faculty")
    with StringUniqueColumn
    with LocalizedLabelColumn {

  def * = (
    id,
    deLabel,
    enLabel
  ) <> (Faculty.tupled, Faculty.unapply)
}
