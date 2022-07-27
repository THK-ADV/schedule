package models

import database.tables.{
  CourseDbEntry,
  SemesterDbEntry,
  SubModuleDbEntry,
  UserDbEntry
}

import java.util.UUID

sealed trait Course extends UniqueEntity {
  def lecturerId: UUID

  def semesterId: UUID

  def subModuleId: UUID

  def interval: CourseInterval

  def courseType: CourseType
}

object Course {
  case class CourseDefault(
      lecturer: UUID,
      semester: UUID,
      subModule: UUID,
      interval: CourseInterval,
      courseType: CourseType,
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
      interval: CourseInterval,
      courseType: CourseType,
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

  def atom(
      db: CourseDbEntry,
      userDb: UserDbEntry,
      semesterDb: SemesterDbEntry,
      subModuleDb: SubModuleDbEntry
  ): CourseAtom = CourseAtom(
    User(userDb),
    Semester(semesterDb),
    SubModule(subModuleDb),
    db.interval,
    db.courseType,
    db.id
  )
}
