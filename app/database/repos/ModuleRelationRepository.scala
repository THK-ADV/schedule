package database.repos

import database.tables.ModuleRelationTable
import models.ModuleRelation
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

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
      _ <- tableQuery.delete
      res <- (tableQuery returning tableQuery) ++= elems
    } yield res
    db.run(q.transactionally)
  }
}
