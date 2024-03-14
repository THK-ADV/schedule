package controllers

import controllers.crud.Read
import models.Module
import play.api.libs.json.Writes
import play.api.mvc.{AbstractController, ControllerComponents}
import service.ModuleService

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class ModuleController @Inject() (
    cc: ControllerComponents,
    val service: ModuleService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Read[UUID, Module] {
  override implicit def writes: Writes[Module] = Module.writes
}
