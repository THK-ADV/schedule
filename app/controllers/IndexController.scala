package controllers

import database.SemesterRepository
import play.api.mvc.{AbstractController, ControllerComponents}

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class IndexController @Inject() (
    cc: ControllerComponents,
    repo: SemesterRepository,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc) {

  def index() = Action.async {
    repo.list().map { ps =>
      Ok(ps.toString())
    }
  }
}
