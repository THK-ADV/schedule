package database.repos

import database.repos.abstracts.{Create, Get}
import database.tables.TeachingUnitTable
import models.TeachingUnit
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class TeachingUnitRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Get[UUID, TeachingUnit, TeachingUnitTable]
    with Create[UUID, TeachingUnit, TeachingUnitTable] {

  import profile.api._

  protected val tableQuery = TableQuery[TeachingUnitTable]

  override protected def uniqueCols(elem: TeachingUnit) =
    List(_.deLabel === elem.deLabel, _.enLabel === elem.enLabel)
}
