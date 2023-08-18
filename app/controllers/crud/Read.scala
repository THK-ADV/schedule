package controllers.crud

import models.UniqueEntity
import play.api.mvc.AbstractController
import service.abstracts.Get

trait Read[ID, Model <: UniqueEntity[ID]] {
  self: AbstractController with JsonHttpResponse[Model] =>

  def service: Get[ID, Model]

  private def parseAtomic(f: Map[String, Seq[String]]): Boolean =
    f.get("atomic")
      .flatMap(_.headOption)
      .flatMap(_.toBooleanOption)
      .getOrElse(false)

  def all() =
    Action.async(r =>
      okSeq(service.all(r.queryString, parseAtomic(r.queryString)))
    )

  def get(id: ID) =
    Action.async(r => okOpt(service.get(id, parseAtomic(r.queryString))))
}
