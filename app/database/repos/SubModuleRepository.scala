/*
package database.repos

import database.tables.SubModuleTable
import models.SubModule
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class SubModuleRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Repository[SubModule, SubModuleTable] {

  import profile.api._

  protected val tableQuery = TableQuery[SubModuleTable]

  override protected def makeFilter = {
    case ("label", vs)        => t => t.hasLabel(vs.head)
    case ("abbreviation", vs) => t => t.hasAbbreviation(vs.head)
    case ("credits", vs)      => t => t.hasCredits(vs.head.toDouble)
  }
}
*/
