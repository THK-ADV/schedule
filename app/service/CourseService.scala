package service

import database.repos.CourseRepository
import database.tables.{CourseDbEntry, CourseTable}
import models.{Course, CourseJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class CourseService @Inject() (val repo: CourseRepository)
    extends Service[CourseJson, Course, CourseDbEntry, CourseTable] {

  override protected def toUniqueDbEntry(json: CourseJson, id: Option[UUID]) =
    CourseDbEntry(
      json.lecturer,
      json.semester,
      json.subModule,
      json.interval,
      json.courseType,
      now(),
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: CourseJson,
      existing: CourseDbEntry
  ): Boolean =
    json.semester == existing.semester &&
      json.lecturer == existing.lecturer &&
      json.subModule == existing.subModule &&
      json.courseType == existing.courseType

  override protected def validate(json: CourseJson) = None

  override protected def uniqueCols(json: CourseJson) = List(
    _.hasSemester(json.semester),
    _.hasUser(json.lecturer),
    _.hasSubModule(json.subModule),
    _.hasCourseType(json.courseType)
  )
}
