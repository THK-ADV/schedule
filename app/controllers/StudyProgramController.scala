package controllers

import database.tables.StudyProgramTable
import models.{StudyProgram, StudyProgramJson}
import play.api.libs.json.{Reads, Writes}
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
    with Controller[StudyProgramJson, StudyProgram, StudyProgramTable] {
  override protected implicit def writes: Writes[StudyProgram] =
    StudyProgram.format

  override protected implicit def reads: Reads[StudyProgramJson] =
    StudyProgramJson.format
}
