package database.repos

import database.repos.filter.{BooleanParser, UUIDParser}
import database.tables.{
  GraduationDbEntry,
  StudyProgramDBEntry,
  StudyProgramTable,
  TeachingUnitDbEntry
}
import models.StudyProgram.StudyProgramAtom
import models.{Graduation, StudyProgram, TeachingUnit}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class StudyProgramRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Repository[StudyProgram, StudyProgramDBEntry, StudyProgramTable]
    with RecursiveQuery[
      StudyProgramDBEntry,
      (StudyProgramDBEntry, TeachingUnitDbEntry, GraduationDbEntry),
      StudyProgramAtom,
      StudyProgramTable
    ]
    with UUIDParser
    with BooleanParser {

  import profile.api._

  val tableQuery = TableQuery[StudyProgramTable]

  override protected def makeFilter = {
    case ("label", vs)        => _.hasLabel(vs.head)
    case ("abbreviation", vs) => _.hasAbbreviation(vs.head)
    case ("graduation", vs)   => t => parseUUID(vs, t.hasGraduation)
    case ("teachingUnit", vs) => t => parseUUID(vs, t.hasTeachingUnit)
    case ("parent", vs)       => t => parseUUID(vs, t.hasParent)
    case ("hasParent", vs) =>
      t => parseBoolean(vs, b => t.parent.isDefined === b)
  }

  override protected def toUniqueEntity(e: StudyProgramDBEntry) =
    StudyProgram(e)

  // TODO this seems to be a good way to collect dependencies of database entries
  def collectDependencies(t: StudyProgramTable) =
    for {
      q <- tableQuery.filter(_.id === t.id)
      tu <- q.teachingUnitFk
      g <- q.graduationFk
    } yield (q, tu, g)

  override protected def retrieveAtom(
      query: Query[StudyProgramTable, StudyProgramDBEntry, Seq]
  ) =
    db.run {
      query
        .flatMap(collectDependencies)
        .result
        .flatMap(getRecursive)
    }

  // RecursiveQuery

  override protected def query(ids: Seq[UUID]) =
    (for {
      sp <- tableQuery.filter(_.id.inSet(ids))
      tu <- sp.teachingUnitFk
      g <- sp.graduationFk
    } yield (sp, tu, g)).result

  override protected def makeAtom(
      lookup: Map[
        UUID,
        (StudyProgramDBEntry, TeachingUnitDbEntry, GraduationDbEntry)
      ],
      e: (StudyProgramDBEntry, TeachingUnitDbEntry, GraduationDbEntry)
  ): StudyProgramAtom =
    StudyProgramAtom(
      TeachingUnit(e._2),
      Graduation(e._3),
      e._1.label,
      e._1.abbreviation,
      e._1.parent
        .flatMap(lookup.get)
        .map(p => makeAtom(lookup, p)),
      e._1.id
    )

  override protected def parentId(e: StudyProgramDBEntry): Option[UUID] =
    e.parent

  override protected def elem(
      es: (StudyProgramDBEntry, TeachingUnitDbEntry, GraduationDbEntry)
  ): StudyProgramDBEntry = es._1
}
