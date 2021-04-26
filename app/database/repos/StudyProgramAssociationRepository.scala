package database.repos

import database.tables.StudyProgramAssociationTable
import models.StudyProgramAssociation
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class StudyProgramAssociationRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Repository[StudyProgramAssociation, StudyProgramAssociationTable] {

  import profile.api._

  protected val tableQuery = TableQuery[StudyProgramAssociationTable]

  override protected def makeFilter = PartialFunction.empty
}
