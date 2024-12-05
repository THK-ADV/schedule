package controllers

import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext

import models.Module
import play.api.cache.Cached
import play.api.libs.json.Json
import play.api.libs.json.Writes
import play.api.mvc.AbstractController
import play.api.mvc.ControllerComponents
import service.ModuleService

@Singleton
final class ModuleController @Inject() (
    cc: ControllerComponents,
    cached: Cached,
    service: ModuleService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc) {
  def all() = cached.status(_.toString, 200, 3600) {
    Action.async { request =>
      val extend = isExtended(request)
      if (extend) service.repo.allFromView().map(Ok(_))
      else service.all().map(xs => Ok(Json.toJson(xs)))
    }
  }
}
