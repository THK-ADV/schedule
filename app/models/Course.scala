package models

import database.tables.CourseDbEntry
import play.api.libs.json.{Json, Writes}

import java.util.UUID

sealed trait Course extends UniqueEntity {
  def lecturerId: UUID

  def semesterId: UUID

  def subModuleId: UUID

  def interval: String

  def courseType: String
}

object Course {
  implicit val writes: Writes[Course] = Writes.apply {
    case default: CourseDefault => writesDefault.writes(default)
    case atom: CourseAtom       => writesAtom.writes(atom)
  }

  implicit val writesDefault: Writes[CourseDefault] = Json.writes[CourseDefault]

  implicit val writesAtom: Writes[CourseAtom] = Json.writes[CourseAtom]

  case class CourseDefault(
      lecturer: UUID,
      semester: UUID,
      subModule: UUID,
      interval: String,
      courseType: String,
      id: UUID
  ) extends Course {
    override def lecturerId = lecturer

    override def semesterId = semester

    override def subModuleId = subModule
  }

  case class CourseAtom(
      lecturer: User,
      semester: Semester,
      subModule: SubModule,
      interval: String,
      courseType: String,
      id: UUID
  ) extends Course {
    override def lecturerId = lecturer.id

    override def semesterId = semester.id

    override def subModuleId = subModule.id
  }

  def apply(db: CourseDbEntry): CourseDefault = CourseDefault(
    db.lecturer,
    db.semester,
    db.subModule,
    db.interval,
    db.courseType,
    db.id
  )
}
