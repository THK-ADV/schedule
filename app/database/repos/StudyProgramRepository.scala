package database.repos

import database.repos.abstracts.{Create, Get}
import database.tables.StudyProgramTable
import models.StudyProgram
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class StudyProgramRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Get[UUID, StudyProgram, StudyProgramTable]
    with Create[UUID, StudyProgram, StudyProgramTable] {

  import profile.api._

  val tableQuery = TableQuery[StudyProgramTable]
}
