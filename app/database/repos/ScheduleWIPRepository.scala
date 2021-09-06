package database.repos

import database.tables.{ScheduleWIPDbEntry, ScheduleWIPTable}
import models.ScheduleWIP
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class ScheduleWIPRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Repository[ScheduleWIP, ScheduleWIPDbEntry, ScheduleWIPTable] {

  import profile.api._

  protected val tableQuery = TableQuery[ScheduleWIPTable]

  override protected val makeFilter = PartialFunction.empty

  override protected def retrieveAtom(
      query: Query[ScheduleWIPTable, ScheduleWIPDbEntry, Seq]
  ) = retrieveDefault(query)

  override protected def toUniqueEntity(e: ScheduleWIPDbEntry) = ScheduleWIP(e)
}
