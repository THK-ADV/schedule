package database.repos

import database.tables.{SubModuleDbEntry, SubModuleTable}
import models.SubModule.SubModuleAtom
import models.{Language, Module, Season, SubModule}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class SubModuleRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Repository[SubModule, SubModuleDbEntry, SubModuleTable]
    with FilterValueParser {

  import profile.api._

  protected val tableQuery = TableQuery[SubModuleTable]

  override protected def makeFilter = {
    case ("label", vs)        => _.hasLabel(vs.head)
    case ("abbreviation", vs) => _.hasAbbreviation(vs.head)
    case ("credits", vs)      => _.hasCredits(vs.head.toDouble)
    case ("language", vs)     => _.hasLanguage(Language(vs.head))
    case ("season", vs)       => _.hasSeason(Season(vs.head))
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
        s.recommendedSemester,
        s.credits,
        s.descriptionUrl,
        s.language,
        s.season,
        s.id
      )
    })

    db.run(action)
  }

  override protected def toUniqueEntity(e: SubModuleDbEntry) = SubModule(e)
}
