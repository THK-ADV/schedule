package database.repos

import database.SQLDateConverter
import database.tables.{
  ExaminationRegulationDbEntry,
  ExaminationRegulationTable
}
import models.{ExaminationRegulation, StudyProgram}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class ExaminationRegulationRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    val spRepo: StudyProgramRepository,
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
      sp <- q.studyProgramFk.flatMap(spRepo.collect)
    } yield (q, sp)

    val action = result.result.map(_.map { case (e, sp) =>
      ExaminationRegulation(e, StudyProgram(sp))
    })

    db.run(action)
  }

  override protected def toUniqueEntity(e: ExaminationRegulationDbEntry) =
    ExaminationRegulation(e)
}
