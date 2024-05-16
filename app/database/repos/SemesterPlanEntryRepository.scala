package database.repos

import database.repos.abstracts.{Create, Get}
import database.tables.SemesterPlanEntryTable
import models.SemesterPlanEntry
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class SemesterPlanEntryRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Get[UUID, SemesterPlanEntry, SemesterPlanEntryTable]
    with Create[UUID, SemesterPlanEntry, SemesterPlanEntryTable] {

  import profile.api._

  protected val tableQuery = TableQuery[SemesterPlanEntryTable]
}
