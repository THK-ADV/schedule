package database.repos

import database.SQLDateConverter
import database.repos.filter.{BooleanParser, UUIDParser}
import database.tables.{
  ModuleExaminationRegulationDbEntry,
  ModuleExaminationRegulationTable
}
import models.{
  ExaminationRegulation,
  Module,
  ModuleExaminationRegulation,
  StudyProgram
}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class ModuleExaminationRegulationRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    val spRepo: StudyProgramRepository,
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

  override protected def retrieveAtom(
      query: Query[
        ModuleExaminationRegulationTable,
        ModuleExaminationRegulationDbEntry,
        Seq
      ]
  ) = {
    val result = for {
      q <- query
      m <- q.moduleFk
      e <- q.examinationRegulationFk
      sp <- e.studyProgramFk.flatMap(spRepo.collect)
    } yield (q, m, e, sp)

    val action = result.result.map(_.map { case (q, m, e, sp) =>
      ModuleExaminationRegulation(
        q,
        Module(m),
        ExaminationRegulation(e, StudyProgram(sp))
      )
    })

    db.run(action)
  }

  override protected def toUniqueEntity(e: ModuleExaminationRegulationDbEntry) =
    ModuleExaminationRegulation(e)
}
