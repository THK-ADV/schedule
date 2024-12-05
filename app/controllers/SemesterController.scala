package controllers

import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext

import models.Semester
import play.api.cache.Cached
import play.api.libs.json.Json
import play.api.libs.json.Writes
import play.api.mvc.AbstractController
import play.api.mvc.ControllerComponents
import service.SemesterService

@Singleton
final class SemesterController @Inject() (
    cc: ControllerComponents,
    service: SemesterService,
    cached: Cached,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc) {
  def all() = cached.status(_.toString, 200, 3600)(
    Action.async(_ => service.all().map(xs => Ok(Json.toJson(xs))))
  )
}
