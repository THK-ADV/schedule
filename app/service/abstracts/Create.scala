package service.abstracts

import database.repos
import models.UniqueEntity

import scala.concurrent.{ExecutionContext, Future}

trait Create[E <: UniqueEntity[_]] {

  def repo: repos.abstracts.Create[_, E, _]

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
