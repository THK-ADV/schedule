package controllers

import controllers.TeachingUnitController.TeachingUnitJson
import controllers.crud.{Create, JsonHttpResponse, Read}
import json.TeachingUnitWrites
import models.TeachingUnit
import play.api.libs.json.{Json, Reads}
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
    with Read[UUID, TeachingUnit]
    with Create[TeachingUnit, TeachingUnitJson]
    with TeachingUnitWrites
    with JsonHttpResponse[TeachingUnit] {
  override implicit def reads: Reads[TeachingUnitJson] = Json.reads

  override def toModel(json: TeachingUnitJson): TeachingUnit =
    TeachingUnit(UUID.randomUUID(), json.faculty, json.deLabel, json.enLabel)
}

object TeachingUnitController {
  case class TeachingUnitJson(faculty: String, deLabel: String, enLabel: String)

  implicit def reads: Reads[TeachingUnitJson] = Json.reads
}
