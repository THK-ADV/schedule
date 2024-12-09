package service.abstracts

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import database.repos
import models.UniqueEntity

trait Create[ID, E <: UniqueEntity[ID]] {

  def repo: repos.abstracts.Create[ID, E, ?]

  implicit def ctx: ExecutionContext

  protected def validate(elem: E): Option[Throwable] = None

  def create(elem: E): Future[E] =
    validate(elem) match {
      case Some(t) =>
        Future.failed(t)
      case None =>
        repo.create(elem)
    }

  def createMany(elems: Iterable[E]) =
    Future.sequence(elems.map(create))
}
