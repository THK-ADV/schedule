package controllers

import controllers.crud.Read
import models.TeachingUnit
import play.api.libs.json.Writes
import play.api.mvc.{AbstractController, ControllerComponents}
import service.TeachingUnitService

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class TeachingUnitController @Inject() (
    cc: ControllerComponents,
    val service: TeachingUnitService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Read[UUID, TeachingUnit] {
  override implicit def writes: Writes[TeachingUnit] = TeachingUnit.writes

  override def all() =
    Action.async { implicit request =>
      val lang = preferredLanguage
      service.repo.allFromView(lang).map(Ok(_))
    }
}
