package database.repos

import database.repos.abstracts.{Create, Get}
import database.tables.StudyProgramTable
import models.StudyProgram
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
final class StudyProgramRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Get[String, StudyProgram, StudyProgram, StudyProgramTable]
    with Create[String, StudyProgram, StudyProgramTable] {

  import profile.api._

  val tableQuery = TableQuery[StudyProgramTable]

  override protected def toUniqueEntity(e: StudyProgram) = e

  override protected def retrieveAtom(
      query: Query[StudyProgramTable, StudyProgram, Seq]
  ): Future[Seq[StudyProgram]] =
    retrieveDefault(query)
}
