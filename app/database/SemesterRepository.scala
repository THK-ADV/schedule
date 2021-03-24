package database

import models.Semester
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SemesterRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ec: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  private class SemesterTable(tag: Tag)
      extends Table[Semester](tag, "semesters") {

    def id = column[UUID]("id", O.PrimaryKey)

    def name = column[String]("name")

    def * = (name, id) <> ((Semester.apply _).tupled, Semester.unapply)
  }

  private val semesters = TableQuery[SemesterTable]

  def create(name: String): Future[Semester] =
    db.run(semesters returning semesters += Semester(name, UUID.randomUUID))

  def list(): Future[Seq[Semester]] = db.run(semesters.result)
}
