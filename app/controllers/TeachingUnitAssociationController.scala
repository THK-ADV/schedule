package controllers

import database.tables.TeachingUnitAssociationTable
import models.{TeachingUnitAssociation, TeachingUnitAssociationJson}
import play.api.libs.json.{Reads, Writes}
import play.api.mvc.{AbstractController, ControllerComponents}
import service.TeachingUnitAssociationService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class TeachingUnitAssociationController @Inject() (
    cc: ControllerComponents,
    val service: TeachingUnitAssociationService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Controller[
      TeachingUnitAssociationJson,
      TeachingUnitAssociation,
      TeachingUnitAssociationTable
    ] {
  override protected implicit def writes: Writes[TeachingUnitAssociation] =
    TeachingUnitAssociation.format

  override protected implicit def reads: Reads[TeachingUnitAssociationJson] =
    TeachingUnitAssociationJson.format
}
