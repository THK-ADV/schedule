package controllers

import controllers.crud.{JsonHttpResponse, Read}
import json.ModuleWrites
import models.Module
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
    with Read[UUID, Module]
    with ModuleWrites
    with JsonHttpResponse[Module]
