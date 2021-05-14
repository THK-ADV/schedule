package service.abstracts

import database.UniqueDbEntry
import models.UniqueEntity

import java.util.UUID
import scala.concurrent.Future

trait Get[Json, Model <: UniqueEntity, DbEntry <: UniqueDbEntry] {
  self: Core[Model, DbEntry, _] =>

  def all(
      filter: Map[String, Seq[String]],
      atomic: Boolean
  ): Future[Seq[Model]] =
    repo.list(filter, atomic)

  def all(atomic: Boolean): Future[Seq[Model]] =
    all(Map.empty, atomic)

  def get(id: UUID, atomic: Boolean): Future[Option[Model]] =
    repo.get(id, atomic)
}
