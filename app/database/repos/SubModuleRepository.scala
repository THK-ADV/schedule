package database.repos

import database.tables.{SubModuleDbEntry, SubModuleTable}
import models.SubModule.SubModuleAtom
import models.{Module, SubModule}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class SubModuleRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Repository[SubModule, SubModuleDbEntry, SubModuleTable] {

  import profile.api._

  protected val tableQuery = TableQuery[SubModuleTable]

  override protected def makeFilter = {
    case ("label", vs)        => t => t.hasLabel(vs.head)
    case ("abbreviation", vs) => t => t.hasAbbreviation(vs.head)
    case ("credits", vs)      => t => t.hasCredits(vs.head.toDouble)
  }

  override protected def retrieveAtom(
      query: Query[SubModuleTable, SubModuleDbEntry, Seq]
  ) = {
    val result = for {
      q <- query
      m <- q.moduleFk
    } yield (q, m)

    val action = result.result.map(_.map { case (s, m) =>
      SubModuleAtom(
        Module(m),
        s.label,
        s.abbreviation,
        s.mandatory,
        s.recommendedSemester,
        s.credits,
        s.descriptionUrl,
        s.id
      )
    })

    db.run(action)
  }

  override protected def toUniqueEntity(e: SubModuleDbEntry) = SubModule(e)
}
