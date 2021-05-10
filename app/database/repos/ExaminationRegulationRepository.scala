package database.repos

import database.SQLDateConverter
import database.tables.{
  ExaminationRegulationDbEntry,
  ExaminationRegulationTable
}
import models.ExaminationRegulation.ExaminationRegulationAtom
import models.{ExaminationRegulation, StudyProgram}
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
    ]
    with SQLDateConverter {

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
  ) = {
    val result = for {
      q <- query
      s <- q.studyProgramFk
    } yield (q, s)

    val action = result.result.map(_.map { case (e, s) =>
      ExaminationRegulationAtom(
        StudyProgram(s),
        e.label,
        e.abbreviation,
        e.start,
        e.end,
        e.id
      )
    })

    db.run(action)
  }

  override protected def toUniqueEntity(e: ExaminationRegulationDbEntry) =
    ExaminationRegulation(e)
}
