package controllers

import controllers.SemesterController.SemesterJson
import controllers.crud.{Create, Read}
import models.Semester
import org.joda.time.LocalDate
import play.api.libs.json.{Json, Reads, Writes}
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
    with Read[UUID, Semester]
    with Create[Semester, SemesterJson] {

  import models.localDateFmt

  override implicit def writes: Writes[Semester] = Semester.writes

  override implicit def reads: Reads[SemesterJson] = Json.reads

  override def toModel(json: SemesterJson): Semester =
    Semester(
      UUID.randomUUID(),
      json.label,
      json.abbrev,
      json.start,
      json.end,
      json.lectureStart,
      json.lectureEnd
    )
}

object SemesterController {
  case class SemesterJson(
      label: String,
      abbrev: String,
      start: LocalDate,
      end: LocalDate,
      lectureStart: LocalDate,
      lectureEnd: LocalDate
  )
}
