package controllers

import controllers.crud.Read
import models.Semester
import play.api.libs.json.Writes
import play.api.mvc.{AbstractController, ControllerComponents}
import service.SemesterService

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class SemesterController @Inject() (
    cc: ControllerComponents,
    val service: SemesterService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Read[UUID, Semester] {
  override implicit def writes: Writes[Semester] = Semester.writes
}
