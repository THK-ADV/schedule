package database.tables

import database.UUIDUniqueColumn
import models.{Course, CourseId}
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

final class CourseTable(tag: Tag)
    extends Table[Course](tag, "course")
    with UUIDUniqueColumn {

  def semester = column[UUID]("semester")

  def module = column[UUID]("module")

  def courseId = column[CourseId]("course_id")

  def * = (
    id,
    semester,
    module,
    courseId
  ) <> ((Course.apply _).tupled, Course.unapply)
}
