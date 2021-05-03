package controllers

import models.{StudyProgramAssociation, StudyProgramAssociationJson}
import play.api.libs.json.{Reads, Writes}
import play.api.mvc.{AbstractController, ControllerComponents}
import service.StudyProgramAssociationService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class StudyProgramAssociationController @Inject() (
    cc: ControllerComponents,
    val service: StudyProgramAssociationService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Controller[StudyProgramAssociationJson, StudyProgramAssociation] {
  override protected implicit def writes: Writes[StudyProgramAssociation] =
    StudyProgramAssociation.format

  override protected implicit def reads: Reads[StudyProgramAssociationJson] =
    StudyProgramAssociationJson.format
}
