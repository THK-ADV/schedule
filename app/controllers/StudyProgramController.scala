package controllers

import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext

import models.StudyProgram
import play.api.cache.Cached
import play.api.libs.json.Json
import play.api.libs.json.Writes
import play.api.mvc.AbstractController
import play.api.mvc.ControllerComponents
import service.StudyProgramService

@Singleton
final class StudyProgramController @Inject() (
    cc: ControllerComponents,
    cached: Cached,
    service: StudyProgramService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc) {

  def all() = cached.status(_.toString, 200, 3600) {
    Action.async { request =>
      val extend = isExtended(request)
      val lang   = preferredLanguage(request)
      if (extend) service.repo.allFromView(lang).map(Ok(_))
      else service.all().map(xs => Ok(Json.toJson(xs)))
    }
  }
}
