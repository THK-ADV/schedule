package database.repos

import database.repos.abstracts.{Create, Get}
import database.tables._
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
    with Get[UUID, ScheduleEntry, ScheduleEntry, ScheduleEntryTable]
    with Create[UUID, ScheduleEntry, ScheduleEntryTable] {

  import profile.api._

  protected val tableQuery = TableQuery[ScheduleEntryTable]

  override protected def retrieveAtom(
      query: Query[ScheduleEntryTable, ScheduleEntry, Seq]
  ) =
    retrieveDefault(query)

  override protected def toUniqueEntity(e: ScheduleEntry) = e
}
