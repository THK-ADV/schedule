package service.abstracts

import database.repos
import models.UniqueEntity

import scala.concurrent.Future

trait Get[ID, Model <: UniqueEntity[ID]] {

  def repo: repos.abstracts.Get[ID, Model, _, _]

  final def all(
      filter: Map[String, Seq[String]],
      atomic: Boolean
  ): Future[Seq[Model]] =
    repo.list(filter, atomic)

  final def all(atomic: Boolean): Future[Seq[Model]] =
    all(Map.empty, atomic)

  def get(id: ID, atomic: Boolean): Future[Option[Model]] =
    repo.get(id, atomic)
}
