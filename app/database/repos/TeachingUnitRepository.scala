package database.repos

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext

import controllers.PreferredLanguage
import database.repos.abstracts.Create
import database.repos.abstracts.Get
import database.tables.TeachingUnitTable
import database.view.JsonView
import models.TeachingUnit
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

@Singleton
final class TeachingUnitRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Get[UUID, TeachingUnit, TeachingUnitTable]
    with Create[UUID, TeachingUnit, TeachingUnitTable]
    with JsonView {

  import profile.api._

  protected val tableQuery = TableQuery[TeachingUnitTable]

  protected override def name: String = "teaching_unit_view"

  def allFromView(lang: PreferredLanguage) =
    getAllFromView(lang)
}
