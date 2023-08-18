package controllers.crud

import models.UniqueEntity
import play.api.libs.json.Reads
import play.api.mvc.AbstractController
import service.abstracts.{Create => C}

trait Create[Model <: UniqueEntity[_], Json] {
  self: AbstractController with JsonHttpResponse[Model] =>

  implicit def reads: Reads[Json]

  def service: C[Model]

  def toModel(json: Json): Model

  def create() = Action.async(parse.json[Json]) { r =>
    created(service.create(toModel(r.body)))
  }
}
