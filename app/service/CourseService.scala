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
      json.subModule == existing.subModule

  override protected def uniqueCols(json: CourseJson, table: CourseTable) =
    List(
      table.hasSemester(json.semester),
      table.hasUser(json.lecturer),
      table.hasSubModule(json.subModule)
    )

  override protected def validate(json: CourseJson) = None
}
