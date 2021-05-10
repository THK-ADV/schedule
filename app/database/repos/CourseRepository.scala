package database.repos

import database.tables.{CourseDbEntry, CourseTable}
import models.Course.CourseAtom
import models.{Course, Semester, SubModule, User}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class CourseRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Repository[Course, CourseDbEntry, CourseTable] {

  import profile.api._

  protected val tableQuery = TableQuery[CourseTable]

  override protected def makeFilter = {
    case ("lecturer", vs)  => t => parseUUID(vs, t.hasUser)
    case ("semester", vs)  => t => parseUUID(vs, t.hasSemester)
    case ("subModule", vs) => t => parseUUID(vs, t.hasSubModule)
  }

  override protected def retrieveAtom(
      query: Query[CourseTable, CourseDbEntry, Seq]
  ) = {
    val result = for {
      q <- query
      u <- q.userFk
      s <- q.semesterFk
      sm <- q.subModuleFk
    } yield (q, u, s, sm)

    val action = result.result.map(_.map { case (c, u, s, sm) =>
      CourseAtom(
        User(u),
        Semester(s),
        SubModule(sm),
        c.interval,
        c.courseType,
        c.id
      )
    })

    db.run(action)
  }

  override protected def toUniqueEntity(e: CourseDbEntry) = Course(e)
}
