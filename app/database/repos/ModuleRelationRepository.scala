package database.repos

import database.tables.ModuleRelationTable
import models.ModuleRelation
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
final class ModuleRelationRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api.*

  protected val tableQuery = TableQuery[ModuleRelationTable]

  def createOrUpdateMany(
      module: UUID,
      elems: List[ModuleRelation]
  ): Future[Int] = {
    val query = for {
      _ <- tableQuery
        .filter(_.parent === module)
        .delete
      size <-
        if elems.nonEmpty then tableQuery.insertAll(elems)
        else DBIO.successful(None)
    } yield size.getOrElse(0)
    db.run(query.transactionally)
  }
}
