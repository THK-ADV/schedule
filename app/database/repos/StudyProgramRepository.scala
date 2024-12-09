package database.repos

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext

import controllers.PreferredLanguage
import database.repos.abstracts.Create
import database.repos.abstracts.Get
import database.tables.StudyProgramTable
import database.view.JsonView
import models.StudyProgram
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

@Singleton
final class StudyProgramRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Get[UUID, StudyProgram, StudyProgramTable]
    with Create[UUID, StudyProgram, StudyProgramTable]
    with JsonView {

  import profile.api._

  val tableQuery = TableQuery[StudyProgramTable]

  protected override def name: String = "study_program_view"

  def allFromView(lang: PreferredLanguage) =
    getAllFromView(lang)
}
