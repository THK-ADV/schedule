package database.repos

import database.tables.{ModuleDbEntry, ModuleTable}
import models.Module.ModuleAtom
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
    with Repository[Module, ModuleDbEntry, ModuleTable]
    with FilterValueParser {

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
      u <- q.userFk
    } yield (q, u)

    val action = result.result.map(_.map { case (q, u) =>
      ModuleAtom(
        User(u),
        q.label,
        q.abbreviation,
        q.credits,
        q.descriptionUrl,
        q.id
      )
    })

    db.run(action)
  }

  override protected def toUniqueEntity(e: ModuleDbEntry) = Module(e)
}
