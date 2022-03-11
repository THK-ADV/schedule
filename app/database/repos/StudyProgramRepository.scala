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

  val tableQuery = TableQuery[StudyProgramTable]

  override protected def makeFilter = {
    case ("label", vs)        => _.hasLabel(vs.head)
    case ("abbreviation", vs) => _.hasAbbreviation(vs.head)
    case ("graduation", vs)   => t => parseUUID(vs, t.hasGraduation)
    case ("teachingUnit", vs) => t => parseUUID(vs, t.hasTeachingUnit)
    case ("parent", vs)       => t => parseUUID(vs, t.hasParent)
  }

  override protected def toUniqueEntity(e: StudyProgramDBEntry) =
    StudyProgram(e)

  // TODO this seems to be a good way to collect dependencies of database entries
  def collect(t: StudyProgramTable) =
    for {
      (q, p) <- tableQuery joinLeft tableQuery on (_.parent === _.id)
      if q.id === t.id
      tu <- q.teachingUnitFk
      g <- q.graduationFk
    } yield (q, tu, g, p)

  override protected def retrieveAtom(
      query: Query[StudyProgramTable, StudyProgramDBEntry, Seq]
  ) =
    db.run {
      query
        .flatMap(collect)
        .result
        .map(_.map { case (sp, tu, g, parent) =>
          StudyProgramAtom(
            TeachingUnit(tu),
            Graduation(g),
            sp.label,
            sp.abbreviation,
            parent.map(StudyProgram.apply),
            sp.id
          )
        })
    }
}
