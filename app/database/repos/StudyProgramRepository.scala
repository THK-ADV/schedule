package database.repos

import database.tables.{StudyProgramDBEntry, StudyProgramTable}
import models.StudyProgram.{StudyProgramAtom, StudyProgramDefault}
import models.{StudyProgram, TeachingUnit}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class StudyProgramRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Repository[StudyProgram, StudyProgramDBEntry, StudyProgramTable] {

  import profile.api._

  protected val tableQuery = TableQuery[StudyProgramTable]

  override protected def makeFilter = {
    case ("label", vs)        => t => t.hasLabel(vs.head)
    case ("abbreviation", vs) => t => t.hasAbbreviation(vs.head)
    case ("graduation", vs)   => t => parseUUID(vs, t.hasGraduation)
    case ("teachingUnit", vs) => t => parseUUID(vs, t.hasTeachingUnit)
  }

  override protected def toUniqueEntity(e: StudyProgramDBEntry) =
    StudyProgramDefault(
      e.teachingUnit,
      e.graduation,
      e.label,
      e.abbreviation,
      e.id
    )

  override protected def retrieveAtom(
      query: Query[StudyProgramTable, StudyProgramDBEntry, Seq]
  ) = {
    val result = for {
      q <- query
      tu <- q.teachingUnitFk
      g <- q.graduationFk
    } yield (q, tu, g)

    val action = result.result.map(_.map { case (sp, tu, g) =>
      val tu0 = TeachingUnit(tu.label, tu.abbreviation, tu.number, tu.id)
      StudyProgramAtom(tu0, g, sp.label, sp.abbreviation, sp.id)
    })

    db.run(action)
  }
}
