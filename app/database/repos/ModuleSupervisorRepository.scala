package database.repos

import database.tables.ModuleSupervisorTable
import models.ModuleSupervisor
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
final class ModuleSupervisorRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api.*

  protected val tableQuery = TableQuery[ModuleSupervisorTable]

  def createOrUpdateMany(
      elems: List[ModuleSupervisor]
  ): Future[Int] = {
    val distinct = elems.distinct
    val modules = distinct.map(_.module)
    val query = for
      _ <- tableQuery.filter(_.module.inSet(modules)).delete
      num <-
        if distinct.nonEmpty then tableQuery.insertAll(distinct)
        else DBIO.successful(None)
    yield num.getOrElse(0)
    db.run(query.transactionally)
  }
}
