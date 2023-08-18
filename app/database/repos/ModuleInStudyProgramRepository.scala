package database.repos

import database.repos.abstracts.{Create, Get}
import database.tables.{
  ModuleInStudyProgramTable,
  ModuleRelationTable,
  ModuleTable,
  StudyProgramTable
}
import models.{Module, ModuleInStudyProgram, ModulePart, Season, StudyProgram}
import org.joda.time.LocalDate
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
final class ModuleInStudyProgramRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Get[
      UUID,
      ModuleInStudyProgram,
      ModuleInStudyProgram,
      ModuleInStudyProgramTable
    ]
    with Create[UUID, ModuleInStudyProgram, ModuleInStudyProgramTable] {

  import profile.api._

  protected val tableQuery = TableQuery[ModuleInStudyProgramTable]

  override protected def retrieveAtom(
      query: Query[ModuleInStudyProgramTable, ModuleInStudyProgram, Seq]
  ): Future[Seq[ModuleInStudyProgram]] =
    retrieveDefault(query)

  override protected def toUniqueEntity(
      e: ModuleInStudyProgram
  ): ModuleInStudyProgram = e

  def deleteAll() = db.run(tableQuery.delete)

  def allInSeason(
      seasons: Seq[Season]
  ): Future[Seq[((Module, ModuleInStudyProgram), StudyProgram)]] = {
    import database.tables.{localDateColumnType, modulePartsColumnType}

    val now = LocalDate.now()
    val seasonIds = seasons.map(_.id)
    val action = TableQuery[ModuleTable]
      .filter(a =>
        a.season.inSet(seasonIds) && !(a.parts === List
          .empty[ModulePart]) && !TableQuery[ModuleRelationTable]
          .filter(_.parent === a.id)
          .exists
      )
      .join(tableQuery)
      .on(_.id === _.module)
      .join(
        TableQuery[StudyProgramTable].filter(s =>
          s.start <= now && (s.end.map(_ >= now) getOrElse true)
        )
      )
      .on(_._2.studyProgram === _.id)
      .result
    db.run(action)
  }
}
