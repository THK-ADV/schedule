package database.repos

import database.tables.{
  ModuleExaminationRegulationDbEntry,
  ModuleExaminationRegulationTable
}
import models.ModuleExaminationRegulation.ModuleExaminationRegulationAtom
import models.{ExaminationRegulation, Module, ModuleExaminationRegulation}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class ModuleExaminationRegulationRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Repository[
      ModuleExaminationRegulation,
      ModuleExaminationRegulationDbEntry,
      ModuleExaminationRegulationTable
    ]
    with FilterValueParser {

  import profile.api._

  protected val tableQuery = TableQuery[ModuleExaminationRegulationTable]

  override protected def makeFilter = {
    case ("module", vs) => t => parseUUID(vs, t.hasModule)
    case ("examinationRegulation", vs) =>
      t => parseUUID(vs, t.hasExaminationRegulation)
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
    } yield (q, m, e)

    val action = result.result.map(_.map { case (q, m, e) =>
      ModuleExaminationRegulationAtom(
        Module(m),
        ExaminationRegulation(e),
        q.mandatory,
        q.id
      )
    })

    db.run(action)
  }

  override protected def toUniqueEntity(e: ModuleExaminationRegulationDbEntry) =
    ModuleExaminationRegulation(e)
}
