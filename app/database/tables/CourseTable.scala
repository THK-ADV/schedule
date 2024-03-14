package database.tables

import database.UUIDUniqueColumn
import models.{Course, ModulePart}
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

final class CourseTable(tag: Tag)
    extends Table[Course](tag, "course")
    with UUIDUniqueColumn {

  def semester = column[UUID]("semester")

  def module = column[UUID]("module")

  def modulePart = column[ModulePart]("part")

  def * = (
    id,
    semester,
    module,
    modulePart
  ) <> ((Course.apply _).tupled, Course.unapply)
}
