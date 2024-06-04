package controllers.crud

import models.UniqueEntity
import play.api.libs.json.{Json, Reads, Writes}
import play.api.mvc.AbstractController
import service.abstracts.{Create => C}

import scala.concurrent.ExecutionContext

trait Create[Model <: UniqueEntity[_], Json] {
  self: AbstractController =>

  implicit def reads: Reads[Json]

  implicit def ctx: ExecutionContext

  implicit def writes: Writes[Model]

  def service: C[Model]

  def toModel(json: Json): Model

  def create() = Action.async(parse.json[Json]) { r =>
    service
      .create(toModel(r.body))
      .map(a => Created(Json.toJson(a)))
  }
}
