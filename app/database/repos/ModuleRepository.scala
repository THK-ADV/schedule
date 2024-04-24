package database.repos

import database.repos.abstracts.{Create, Get}
import database.tables.ModuleTable
import models.Module
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.{GetResult, JdbcProfile}

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
final class ModuleRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Get[UUID, Module, ModuleTable]
    with Create[UUID, Module, ModuleTable] {

  import profile.api._

  val tableQuery = TableQuery[ModuleTable]

  implicit def jsonResult: GetResult[String] =
    GetResult(_.nextString())

  def getAllFromView(): Future[String] =
    db.run(sql"select * from module_view".as).map(_.head)

  def deleteAll() =
    db.run(tableQuery.delete)
}
