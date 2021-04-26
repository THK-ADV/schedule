package controllers

import database.cols.IDColumn
import models.UniqueEntity
import play.api.libs.json.Reads
import play.api.mvc.AbstractController
import service.abstracts.Service
import slick.jdbc.PostgresProfile.api.Table

import java.util.UUID

trait Controller[Json, Model <: UniqueEntity, T <: Table[Model] with IDColumn]
    extends JsonHttpResponse[Model] {
  self: AbstractController =>

  protected def service: Service[Json, Model, T]

  protected implicit def reads: Reads[Json]

  def all() = Action.async { r =>
    okSeq(service.all(r.queryString))
  }

  def get(id: UUID) = Action.async {
    okOpt(service.get(id))
  }

  def delete(id: UUID) = Action.async {
    ok(service.delete(id))
  }

  def create() = Action.async(parse.json[Json]) { r =>
    created(service.create(r.body))
  }

  def update(id: UUID) = Action.async(parse.json[Json]) { r =>
    ok(service.update(r.body, id))
  }
}
