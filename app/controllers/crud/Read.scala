package controllers.crud

import scala.concurrent.ExecutionContext

import models.UniqueEntity
import play.api.libs.json.Json
import play.api.libs.json.Writes
import play.api.mvc.AbstractController
import service.abstracts.Get

trait Read[ID, Model <: UniqueEntity[ID]] {
  self: AbstractController =>

  implicit def ctx: ExecutionContext

  implicit def writes: Writes[Model]

  def service: Get[ID, Model]

  def all() =
    Action.async { r =>
      val res =
        if (r.queryString.isEmpty) service.all()
        else service.allWithFilter(r.queryString)
      res.map(xs => Ok(Json.toJson(xs)))
    }

  def get(id: ID) =
    Action.async { r =>
      service
        .get(id)
        .map {
          case Some(a) => Ok(Json.toJson(a))
          case None    => NotFound
        }
    }
}
