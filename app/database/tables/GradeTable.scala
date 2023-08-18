package database.tables

import database.cols.{
  LocalizedDescColumn,
  LocalizedLabelColumn,
  StringUniqueColumn
}
import models.Grade
import slick.jdbc.PostgresProfile.api._

class GradeTable(tag: Tag)
    extends Table[Grade](tag, "grade")
    with StringUniqueColumn
    with LocalizedLabelColumn
    with LocalizedDescColumn {

  def * = (
    id,
    deLabel,
    enLabel,
    deDesc,
    enDesc
  ) <> (Grade.tupled, Grade.unapply)
}
