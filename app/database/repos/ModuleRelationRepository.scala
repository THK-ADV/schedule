package database.repos

import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import database.tables.ModuleRelationTable
import models.ModuleRelation
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

@Singleton
final class ModuleRelationRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  protected val tableQuery = TableQuery[ModuleRelationTable]

  def createOrUpdateMany(
      elems: List[ModuleRelation]
  ): Future[Seq[ModuleRelation]] = {
    val q = for {
      _   <- tableQuery.delete
      res <- (tableQuery.returning(tableQuery)) ++= elems
    } yield res
    db.run(q.transactionally)
  }
}
