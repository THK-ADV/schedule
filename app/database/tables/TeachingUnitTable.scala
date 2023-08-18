package database.tables

import database.cols._
import models.TeachingUnit
import slick.jdbc.PostgresProfile.api._

final class TeachingUnitTable(tag: Tag)
    extends Table[TeachingUnit](tag, "teaching_unit")
    with UUIDUniqueColumn
    with LocalizedLabelColumn
    with FacultyColumn {

  def * = (
    id,
    faculty,
    deLabel,
    enLabel
  ) <> (TeachingUnit.tupled, TeachingUnit.unapply)
}
