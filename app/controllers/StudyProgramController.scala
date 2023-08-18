package controllers

import controllers.crud.{JsonHttpResponse, Read}
import json.StudyProgramWrites
import models.StudyProgram
import play.api.mvc.{AbstractController, ControllerComponents}
import service.StudyProgramService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class StudyProgramController @Inject() (
    cc: ControllerComponents,
    val service: StudyProgramService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Read[String, StudyProgram]
    with StudyProgramWrites
    with JsonHttpResponse[StudyProgram]
