package database.repos

import database.repos.filter.UUIDParser
import database.tables.{StudyProgramDBEntry, StudyProgramTable}
import models.StudyProgram.StudyProgramAtom
import models.{Graduation, StudyProgram, TeachingUnit}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class StudyProgramRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Repository[StudyProgram, StudyProgramDBEntry, StudyProgramTable]
    with UUIDParser {

  import profile.api._

  protected val tableQuery = TableQuery[StudyProgramTable]

  override protected def makeFilter = {
    case ("label", vs)        => t => t.hasLabel(vs.head)
    case ("abbreviation", vs) => t => t.hasAbbreviation(vs.head)
    case ("graduation", vs)   => t => parseUUID(vs, t.hasGraduation)
    case ("teachingUnit", vs) => t => parseUUID(vs, t.hasTeachingUnit)
  }

  override protected def toUniqueEntity(e: StudyProgramDBEntry) =
    StudyProgram(e)

  override protected def retrieveAtom(
      query: Query[StudyProgramTable, StudyProgramDBEntry, Seq]
  ) = {
    val result = for {
      q <- query
      tu <- q.teachingUnitFk
      g <- q.graduationFk
    } yield (q, tu, g)

    val action = result.result.map(_.map { case (sp, tu, g) =>
      StudyProgramAtom(
        TeachingUnit(tu),
        Graduation(g),
        sp.label,
        sp.abbreviation,
        sp.id
      )
    })

    db.run(action)
  }
}
