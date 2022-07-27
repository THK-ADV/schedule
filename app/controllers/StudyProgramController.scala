package controllers

import json.StudyProgramFormat
import models.{StudyProgram, StudyProgramJson}
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}
import service.StudyProgramService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class StudyProgramController @Inject() (
    cc: ControllerComponents,
    val service: StudyProgramService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Controller[StudyProgramJson, StudyProgram]
    with StudyProgramFormat.All {
  override protected implicit def writes: Writes[StudyProgram] =
    studyProgramWrites

  override protected implicit def reads: Reads[StudyProgramJson] =
    studyProgramJsonFmt
}
