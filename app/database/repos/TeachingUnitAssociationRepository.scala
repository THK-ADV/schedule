package database.repos

import database.tables.{
  TeachingUnitAssociationDbEntry,
  TeachingUnitAssociationTable
}
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
    with Repository[
      TeachingUnitAssociation,
      TeachingUnitAssociationDbEntry,
      TeachingUnitAssociationTable
    ] {

  import profile.api._

  protected val tableQuery = TableQuery[TeachingUnitAssociationTable]

  override protected def makeFilter = PartialFunction.empty

  override protected def retrieveAtom(
      query: Query[
        TeachingUnitAssociationTable,
        TeachingUnitAssociationDbEntry,
        Seq
      ]
  ) =
    retrieveDefault(query)

  override protected def toUniqueEntity(e: TeachingUnitAssociationDbEntry) =
    TeachingUnitAssociation(e.faculty, e.teachingUnit, e.id)
}
