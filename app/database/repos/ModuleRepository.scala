package database.repos

import database.tables.{ModuleDbEntry, ModuleTable}
import models.Module.{ModuleAtom, ModuleDefault}
import models.{ExaminationRegulation, Module, User}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class ModuleRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Repository[Module, ModuleDbEntry, ModuleTable] {

  import profile.api._

  protected val tableQuery = TableQuery[ModuleTable]

  override protected def makeFilter = {
    case ("label", vs)         => t => t.hasLabel(vs.head)
    case ("abbreviation", vs)  => t => t.hasAbbreviation(vs.head)
    case ("credits", vs)       => t => t.hasCredits(vs.head.toDouble)
    case ("courseManager", vs) => t => parseUUID(vs, t.hasUser)
  }

  override protected def retrieveAtom(
      query: Query[ModuleTable, ModuleDbEntry, Seq]
  ) = {
    val result = for {
      q <- query
      e <- q.examinationRegulationFk
      u <- q.userFk
    } yield (q, e, u)

    val action = result.result.map(_.map { case (m, e, u) =>
      ModuleAtom(
        ExaminationRegulation(e),
        User(u),
        m.label,
        m.abbreviation,
        m.credits,
        m.descriptionUrl,
        m.id
      )
    })

    db.run(action)
  }

  override protected def toUniqueEntity(e: ModuleDbEntry) = ModuleDefault(
    e.examinationRegulation,
    e.courseManager,
    e.label,
    e.abbreviation,
    e.credits,
    e.descriptionUrl,
    e.id
  )
}
