package database.tables

import java.util.UUID

import database.UUIDUniqueColumn
import models.Course
import models.CourseId
import slick.jdbc.PostgresProfile.api._

final class CourseTable(tag: Tag) extends Table[Course](tag, "course") with UUIDUniqueColumn {

  def semester = column[UUID]("semester")

  def module = column[UUID]("module")

  def courseId = column[CourseId]("course_id")

  def * = (
    id,
    semester,
    module,
    courseId
  ) <> (Course.apply, Course.unapply)
}
