package controllers

import controllers.TeachingUnitController.TeachingUnitJson
import controllers.crud.{Create, Read}
import models.TeachingUnit
import play.api.libs.json.{Json, Reads, Writes}
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
    with Create[TeachingUnit, TeachingUnitJson] {
  override implicit def reads: Reads[TeachingUnitJson] = Json.reads

  override def toModel(json: TeachingUnitJson): TeachingUnit =
    TeachingUnit(UUID.randomUUID(), json.faculty, json.deLabel, json.enLabel)

  override implicit def writes: Writes[TeachingUnit] = TeachingUnit.writes
}

object TeachingUnitController {
  case class TeachingUnitJson(faculty: String, deLabel: String, enLabel: String)

  implicit def reads: Reads[TeachingUnitJson] = Json.reads
}
