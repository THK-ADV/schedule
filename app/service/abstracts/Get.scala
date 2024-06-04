package service.abstracts

import database.repos
import models.UniqueEntity

import scala.concurrent.Future

trait Get[ID, Model <: UniqueEntity[ID]] {

  def repo: repos.abstracts.Get[ID, Model, _]

  final def allWithFilter(
      filter: Map[String, Seq[String]]
  ): Future[Seq[Model]] =
    repo.allWithFilter(filter)

  final def all(): Future[Seq[Model]] =
    repo.all()

  final def get(id: ID): Future[Option[Model]] =
    repo.getOpt(id)
}
