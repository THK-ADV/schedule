package controllers

import models.SemesterJson
import play.api.mvc.{AbstractController, ControllerComponents}
import service.SemesterService

import java.util.UUID
import javax.inject.Inject
import scala.concurrent.ExecutionContext

class SemesterController @Inject() (
    cc: ControllerComponents,
    service: SemesterService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with JsonHttpResponse {

  def all() = Action.async { r =>
    okSeq(service.all(r.queryString))
  }

  def delete(id: UUID) = Action.async {
    ok(service.delete(id))
  }

  def create() = Action.async(parse.json[SemesterJson]) { r =>
    created(service.create(r.body))
  }

  def update(id: UUID) = Action.async(parse.json[SemesterJson]) { r =>
    ok(service.update(r.body, id))
  }
}
