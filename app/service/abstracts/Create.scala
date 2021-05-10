package service.abstracts

import database.UniqueDbEntry
import database.cols.UniqueEntityColumn
import models.UniqueEntity
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

trait Create[Json, Model <: UniqueEntity, DbEntry <: UniqueDbEntry, T <: Table[
  DbEntry
] with UniqueEntityColumn] {
  self: JsonConverter[Json, DbEntry] with Core[Model, DbEntry, T] =>

  protected def validate(json: Json): Option[Throwable]

  protected def uniqueCols(json: Json, table: T): List[Rep[Boolean]]

  def create(json: Json): Future[Model] =
    validate(json) match {
      case Some(t) =>
        Future.failed(t)
      case None =>
        repo.create(toUniqueDbEntry(json, None), t => uniqueCols(json, t))
    }
}
