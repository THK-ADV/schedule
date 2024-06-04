package database.repos

import database.repos.abstracts.Get
import database.tables._
import database.view.ScheduleEntryViewRefresher
import models._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class ScheduleEntryRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Get[UUID, ScheduleEntry, ScheduleEntryTable]
    with ScheduleEntryViewRefresher {

  import profile.api._

  protected val tableQuery = TableQuery[ScheduleEntryTable]

  private val spTableQuery = TableQuery[ModuleStudyProgramScheduleEntryTable]

  private val rTableQuery = TableQuery[ScheduleEntryRoomTable]

  def createMany(
      entries: Seq[ScheduleEntry],
      studyProgramAssoc: Seq[ModuleStudyProgramScheduleEntry],
      rooms: Seq[ScheduleEntryRoom]
  ) = {
    val q = for {
      _ <- tableQuery ++= entries
      _ <- spTableQuery ++= studyProgramAssoc
      _ <- rTableQuery ++= rooms
      _ <- refreshView()
    } yield ()
    db.run(q.transactionally)
  }

  def deleteAll() = {
    val q = for {
      _ <- rTableQuery.delete
      _ <- spTableQuery.delete
      _ <- tableQuery.delete
      _ <- refreshView()
    } yield ()
    db.run(q.transactionally)
  }
}
