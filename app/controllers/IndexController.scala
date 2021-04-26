package controllers

import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import javax.inject.{Inject, Singleton}

@Singleton
class IndexController @Inject() (cc: ControllerComponents)
    extends AbstractController(cc) {
  def index() = Action {
    Ok(Json.obj("msg" -> "it works"))
  }
}
