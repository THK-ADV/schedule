package database.repos

import database.repos.abstracts.{Create, Get}
import database.tables.ModuleTable
import models.Module
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
final class ModuleRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Get[UUID, Module, Module, ModuleTable]
    with Create[UUID, Module, ModuleTable] {

  import profile.api._

  val tableQuery = TableQuery[ModuleTable]

  override protected def retrieveAtom(
      query: Query[ModuleTable, Module, Seq]
  ): Future[Seq[Module]] =
    retrieveDefault(query)

  override protected def toUniqueEntity(e: Module): Module = e
}
