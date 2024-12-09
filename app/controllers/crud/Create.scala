package controllers.crud

import scala.concurrent.ExecutionContext

import models.UniqueEntity
import play.api.libs.json.Json
import play.api.libs.json.Reads
import play.api.libs.json.Writes
import play.api.mvc.AbstractController
import service.abstracts.{ Create => C }

trait Create[ID, Model <: UniqueEntity[ID], Json] {
  self: AbstractController =>

  implicit def reads: Reads[Json]

  implicit def ctx: ExecutionContext

  implicit def writes: Writes[Model]

  def service: C[ID, Model]

  def toModel(json: Json): Model

  def create() = Action.async(parse.json[Json]) { r =>
    service
      .create(toModel(r.body))
      .map(a => Created(Json.toJson(a)))
  }
}
