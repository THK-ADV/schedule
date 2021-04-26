package service.abstracts

import database.cols.IDColumn
import models.UniqueEntity
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

trait Create[Json, Model <: UniqueEntity, T <: Table[Model] with IDColumn] {
  self: JsonConverter[Json, Model] with Core[Model, T] =>

  protected def validate(json: Json): Option[Throwable] = None

  protected def uniqueCols(json: Json, table: T): List[Rep[Boolean]]

  def create(json: Json): Future[Model] =
    validate(json) match {
      case Some(t) =>
        Future.failed(t)
      case None =>
        repo.create(toModel(json, None), t => uniqueCols(json, t))
    }
}
