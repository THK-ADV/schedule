package controllers

import controllers.crud.{JsonHttpResponse, Read}
import json.ModuleInStudyProgramWrites
import models.ModuleInStudyProgram
import play.api.mvc.{AbstractController, ControllerComponents}
import service.ModuleInStudyProgramService

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class ModuleInStudyProgramController @Inject() (
    cc: ControllerComponents,
    val service: ModuleInStudyProgramService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Read[UUID, ModuleInStudyProgram]
    with ModuleInStudyProgramWrites
    with JsonHttpResponse[ModuleInStudyProgram]
