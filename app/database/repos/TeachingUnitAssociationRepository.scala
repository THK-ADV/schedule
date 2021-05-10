/*
package database.repos

import database.tables.TeachingUnitAssociationTable
import models.TeachingUnitAssociation
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class TeachingUnitAssociationRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Repository[TeachingUnitAssociation, TeachingUnitAssociationTable] {

  import profile.api._

  protected val tableQuery = TableQuery[TeachingUnitAssociationTable]

  override protected def makeFilter = PartialFunction.empty
}
*/
