/*
package database.repos

import database.tables.TeachingUnitTable
import models.TeachingUnit
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class TeachingUnitRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Repository[TeachingUnit, TeachingUnitTable] {

  import profile.api._

  protected val tableQuery = TableQuery[TeachingUnitTable]

  override protected def makeFilter = {
    case ("label", vs)        => t => t.hasLabel(vs.head)
    case ("abbreviation", vs) => t => t.hasAbbreviation(vs.head)
  }
}
*/
