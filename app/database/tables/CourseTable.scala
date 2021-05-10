package database.tables

import database.UniqueDbEntry
import database.cols.{
  SemesterColumn,
  SubModuleColumn,
  UniqueEntityColumn,
  UserColumn
}
import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp
import java.util.UUID

case class CourseDbEntry(
    lecturer: UUID,
    semester: UUID,
    subModule: UUID,
    interval: String,
    courseType: String,
    lastModified: Timestamp,
    id: UUID
) extends UniqueDbEntry

class CourseTable(tag: Tag)
    extends Table[CourseDbEntry](tag, "course")
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
    lastModified,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: (
      (UUID, UUID, UUID, String, String, Timestamp, UUID)
  ) => CourseDbEntry = {
    case (
          lecturer,
          semester,
          subModule,
          interval,
          courseType,
          lastModified,
          id
        ) =>
      CourseDbEntry(
        lecturer,
        semester,
        subModule,
        interval,
        courseType,
        lastModified,
        id
      )
  }

  def unmapRow: CourseDbEntry => Option[
    (UUID, UUID, UUID, String, String, Timestamp, UUID)
  ] =
    a =>
      Option(
        (
          a.lecturer,
          a.semester,
          a.subModule,
          a.interval,
          a.courseType,
          a.lastModified,
          a.id
        )
      )
}
