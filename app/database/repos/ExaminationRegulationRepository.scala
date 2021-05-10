package database.repos

import database.tables.{
  ExaminationRegulationDbEntry,
  ExaminationRegulationTable
}
import models.ExaminationRegulation
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class ExaminationRegulationRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Repository[
      ExaminationRegulation,
      ExaminationRegulationDbEntry,
      ExaminationRegulationTable
    ] {

  import profile.api._

  protected val tableQuery = TableQuery[ExaminationRegulationTable]

  override protected def makeFilter = {
    case ("label", vs)        => t => t.hasLabel(vs.head)
    case ("abbreviation", vs) => t => t.hasAbbreviation(vs.head)
  }

  override protected def retrieveAtom(
      query: Query[
        ExaminationRegulationTable,
        ExaminationRegulationDbEntry,
        Seq
      ]
  ) = ???

  override protected def toUniqueEntity(e: ExaminationRegulationDbEntry) =
    ExaminationRegulation(
      e.studyProgram,
      e.label,
      e.abbreviation,
      e.start,
      e.end,
      e.id
    )
}
