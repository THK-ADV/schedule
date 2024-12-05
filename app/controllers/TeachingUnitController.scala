package controllers

import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext

import play.api.cache.Cached
import play.api.mvc.AbstractController
import play.api.mvc.ControllerComponents
import service.TeachingUnitService

@Singleton
final class TeachingUnitController @Inject() (
    cc: ControllerComponents,
    cached: Cached,
    service: TeachingUnitService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc) {

  def all() =
    cached.status(_.toString, 200, 3600) {
      Action.async { implicit request =>
        val lang = preferredLanguage
        service.repo.allFromView(lang).map(Ok(_))
      }
    }
}
