package controllers

import controllers.crud.Read
import models.StudyProgram
import play.api.libs.json.Writes
import play.api.mvc.{AbstractController, ControllerComponents}
import service.StudyProgramService

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class StudyProgramController @Inject() (
    cc: ControllerComponents,
    val service: StudyProgramService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Read[UUID, StudyProgram] {
  override implicit def writes: Writes[StudyProgram] = StudyProgram.writes
}
