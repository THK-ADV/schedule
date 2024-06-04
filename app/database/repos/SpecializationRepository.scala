package database.repos

import database.repos.abstracts.{Create, Get}
import database.tables.SpecializationTable
import models.Specialization
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class SpecializationRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Get[String, Specialization, SpecializationTable]
    with Create[String, Specialization, SpecializationTable] {

  import profile.api._

  protected val tableQuery = TableQuery[SpecializationTable]
}
