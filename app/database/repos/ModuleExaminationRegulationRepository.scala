package database.repos

import database.SQLDateConverter
import database.repos.filter.{BooleanParser, UUIDParser}
import database.tables.{
  ExaminationRegulationDbEntry,
  ModuleDbEntry,
  ModuleExaminationRegulationDbEntry,
  ModuleExaminationRegulationTable
}
import models.ModuleExaminationRegulation.ModuleExaminationRegulationAtom
import models.{Module, ModuleExaminationRegulation, StudyProgram}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class ModuleExaminationRegulationRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    val spRepo: StudyProgramRepository,
    val examRepo: ExaminationRegulationRepository,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Repository[
      ModuleExaminationRegulation,
      ModuleExaminationRegulationDbEntry,
      ModuleExaminationRegulationTable
    ]
    with UUIDParser
    with SQLDateConverter
    with BooleanParser {

  import profile.api._

  protected val tableQuery = TableQuery[ModuleExaminationRegulationTable]

  override protected def makeFilter = {
    case ("module", vs) => t => parseUUID(vs, t.isModule)
    case ("examinationRegulation", vs) =>
      t => parseUUID(vs, t.examinationRegulation)
    case ("mandatory", vs) =>
      t => parseBoolean(vs, t.isMandatory)
  }

  def collectDependencies(t: ModuleExaminationRegulationTable) =
    for {
      q <- tableQuery.filter(_.id === t.id)
      m <- q.moduleFk
      e <- q.examinationRegulationFk.flatMap(examRepo.collectDependencies)
    } yield (q, m, e)

  def makeAtom(
      mer: ModuleExaminationRegulationDbEntry,
      m: ModuleDbEntry,
      e: ExaminationRegulationDbEntry,
      sp: StudyProgram.StudyProgramAtom
  ) =
    ModuleExaminationRegulationAtom(
      Module(m),
      examRepo.makeAtom(e, sp),
      mer.mandatory,
      mer.id
    )

  override protected def retrieveAtom(
      query: Query[
        ModuleExaminationRegulationTable,
        ModuleExaminationRegulationDbEntry,
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
                .getRecursive(Seq(e._3._2))
                .map(sp => makeAtom(e._1, e._2, e._3._1, sp.head))
            }
          )
        }
    }

  override protected def toUniqueEntity(e: ModuleExaminationRegulationDbEntry) =
    ModuleExaminationRegulation(e)
}
