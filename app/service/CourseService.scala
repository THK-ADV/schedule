package service

import database.repos.CourseRepository
import database.tables.{CourseDbEntry, CourseTable}
import models.Course.CourseAtom
import models.{Course, CourseJson, CourseType}
import service.abstracts.Service
import slick.lifted.Rep

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CourseService @Inject() (
    val repo: CourseRepository,
    implicit val ctx: ExecutionContext
) extends Service[CourseJson, Course, CourseDbEntry, CourseTable] {

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

  override protected def uniqueCols(json: CourseJson) =
    checkUniqueCols(
      json.semester,
      json.lecturer,
      json.subModule,
      json.courseType
    )

  def checkUniqueCols(
      semester: UUID,
      user: UUID,
      subModule: UUID,
      courseType: CourseType
  ): List[CourseTable => Rep[Boolean]] = List(
    _.semester(semester),
    _.user(user),
    _.subModule(subModule),
    _.hasCourseType(courseType)
  )

  def allAtoms(filter: Map[String, Seq[String]]) =
    all(filter, atomic = true).map(
      _.map(_.asInstanceOf[CourseAtom])
    )
}
