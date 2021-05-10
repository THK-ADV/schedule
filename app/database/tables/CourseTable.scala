package database.tables

import database.cols.{
  UniqueEntityColumn,
  SemesterColumn,
  SubModuleColumn,
  UserColumn
}
import models.Course
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

class CourseTable(tag: Tag)
    extends Table[Course](tag, "course")
    with UniqueEntityColumn
    with UserColumn
    with SemesterColumn
    with SubModuleColumn {

  override protected def userColumnName = "lecturer"

  def interval = column[String]("interval")

  def courseType = column[String]("course_type")

  def * = (
    user,
    semester,
    subModule,
    interval,
    courseType,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: ((UUID, UUID, UUID, String, String, UUID)) => Course = {
    case (lecturer, semester, subModule, interval, courseType, id) =>
      Course(lecturer, semester, subModule, interval, courseType, id)
  }

  def unmapRow: Course => Option[(UUID, UUID, UUID, String, String, UUID)] =
    a =>
      Option(
        (a.lecturer, a.semester, a.subModule, a.interval, a.courseType, a.id)
      )
}
