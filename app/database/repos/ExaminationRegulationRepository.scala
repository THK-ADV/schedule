package database.repos

import database.SQLDateConverter
import database.tables.{
  ExaminationRegulationDbEntry,
  ExaminationRegulationTable
}
import models.ExaminationRegulation
import models.ExaminationRegulation.ExaminationRegulationAtom
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

  override protected def makeFilter = PartialFunction.empty

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
      tu <- s.teachingUnitFk
      g <- s.graduationFk
    } yield (q, s, tu, g)

    val action = result.result.map(_.map { case (e, s, tu, g) =>
      ExaminationRegulationAtom(e, s, tu, g)
    })

    db.run(action)
  }

  override protected def toUniqueEntity(e: ExaminationRegulationDbEntry) =
    ExaminationRegulation(e)
}
