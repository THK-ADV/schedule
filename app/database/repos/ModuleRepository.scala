package database.repos

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext

import database.repos.abstracts.Create
import database.repos.abstracts.Get
import database.tables.ModuleTable
import database.view.JsonView
import models.Module
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

@Singleton
final class ModuleRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Get[UUID, Module, ModuleTable]
    with Create[UUID, Module, ModuleTable]
    with JsonView {

  import profile.api._

  val tableQuery = TableQuery[ModuleTable]

  protected override def name: String = "module_view"

  def deleteAll() =
    db.run(tableQuery.delete)

  def allFromView() =
    getAllFromView
}
