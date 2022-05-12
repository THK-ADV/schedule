package database.repos

import database.repos.filter.UUIDParser
import database.tables._
import models.Course
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class CourseRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Repository[Course, CourseDbEntry, CourseTable]
    with UUIDParser {

  import profile.api._

  protected val tableQuery = TableQuery[CourseTable]

  override protected def makeFilter = {
    case ("lecturer", vs)  => t => parseUUID(vs, t.user)
    case ("semester", vs)  => t => parseUUID(vs, t.semester)
    case ("subModule", vs) => t => parseUUID(vs, t.subModule)
  }

  def collectDependencies(t: CourseTable) =
    for {
      q <- tableQuery.filter(_.id === t.id)
      u <- q.userFk
      s <- q.semesterFk
      sm <- q.subModuleFk
    } yield (q, u, s, sm)

  def makeAtom(
      c: (CourseDbEntry, UserDbEntry, SemesterDbEntry, SubModuleDbEntry)
  ) =
    Course.atom(c._1, c._2, c._3, c._4)

  override protected def retrieveAtom(
      query: Query[CourseTable, CourseDbEntry, Seq]
  ) =
    db.run {
      query
        .flatMap(collectDependencies)
        .result
        .map(_.map(makeAtom))
    }

  override protected def toUniqueEntity(e: CourseDbEntry) = Course(e)
}
