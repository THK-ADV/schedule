package database.repos

import database.repos.abstracts.{Create, Get}
import database.tables.ReservationTable
import models.Reservation
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class ReservationRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Get[UUID, Reservation, Reservation, ReservationTable]
    with Create[UUID, Reservation, ReservationTable] {

  import profile.api._

  protected val tableQuery = TableQuery[ReservationTable]

  override protected def retrieveAtom(
      query: Query[ReservationTable, Reservation, Seq]
  ) =
    retrieveDefault(query)

  override protected def toUniqueEntity(e: Reservation) = e
}
