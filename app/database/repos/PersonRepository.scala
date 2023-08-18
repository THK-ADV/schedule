package database.repos

import database.repos.abstracts.{Create, Get}
import database.tables.PersonTable
import models.Person
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class PersonRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Get[String, Person, Person, PersonTable]
    with Create[String, Person, PersonTable] {

  import profile.api._

  protected val tableQuery = TableQuery[PersonTable]

  override protected def retrieveAtom(
      query: Query[PersonTable, Person, Seq]
  ) =
    retrieveDefault(query)

  override protected def toUniqueEntity(e: Person) = e
}
