package controllers

import play.api.libs.json.JsBoolean
import play.api.mvc.{AbstractController, ControllerComponents}

import javax.inject.Inject

class DataImportController @Inject() (cc: ControllerComponents)
    extends AbstractController(cc) {

  def imports() = Action(parse.text) { r =>
    r.body.linesIterator.toVector.foreach(parseLine)
    Ok(JsBoolean(true))
  }

  private def parseLine(s: String) = {
    s.split(";").foreach(println)
    println("===")
  }
}
