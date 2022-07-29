package database.repos

import database.SQLDateConverter
import database.tables.{
  ExaminationRegulationDbEntry,
  ExaminationRegulationTable
}
import models.ExaminationRegulation.{ExaminationRegulationAtom, toLocalDate}
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

  override protected val makeFilter = { case ("studyProgram_label", vs) =>
    _.studyProgramLabel(vs.head)
  }

  def collectDependencies(t: ExaminationRegulationTable) =
    for {
      q <- tableQuery.filter(_.id === t.id)
      sp <- q.studyProgramFk.flatMap(spRepo.collectDependencies)
    } yield (q, sp)

  def makeAtom(
      e: ExaminationRegulationDbEntry,
      sp: StudyProgram.StudyProgramAtom
  ) =
    ExaminationRegulationAtom(
      sp,
      e.number,
      e.start,
      e.end.map(toLocalDate),
      e.id
    )

  override protected def retrieveAtom(
      query: Query[
        ExaminationRegulationTable,
        ExaminationRegulationDbEntry,
        Seq
      ]
  ) =
    db.run {
      query
        .flatMap(collectDependencies)
        .result
        .flatMap { elems =>
          DBIO.sequence(
            elems.map { e =>
              spRepo
                .getRecursive(Seq(e._2))
                .map(sp => makeAtom(e._1, sp.head))
            }
          )
        }
    }

  override protected def toUniqueEntity(e: ExaminationRegulationDbEntry) =
    ExaminationRegulation(e)
}
