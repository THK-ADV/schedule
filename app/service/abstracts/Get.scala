package service.abstracts

import models.UniqueEntity

import java.util.UUID
import scala.concurrent.Future

trait Get[Json, Model <: UniqueEntity] {
  self: Core[Model, _] =>

  def all(filter: Map[String, Seq[String]]): Future[Seq[Model]] =
    repo.list(filter)

  def get(id: UUID): Future[Option[Model]] =
    repo.get(id)
}
