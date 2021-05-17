package models

import play.api.libs.json._

import scala.util.{Failure, Success, Try}

sealed trait Language

object Language {
  implicit val writes: Writes[Language] =
    Writes[Language](unapply _ andThen JsString)

  implicit val reads: Reads[Language] = Reads(
    _.validate[String].flatMap(s => JsResult.fromTry(apply(s)))
  )

  def apply(string: String): Try[Language] = string match {
    case "en"              => Success(EN)
    case "de"              => Success(DE)
    case "de_en" | "en_de" => Success(DE_EN)
    case _ =>
      Failure(new Throwable(s"expected en, de or de_en, but was $string"))
  }

  def unapply(lang: Language): String = lang match {
    case EN    => "en"
    case DE    => "de"
    case DE_EN => "de_en"
  }

  case object EN extends Language

  case object DE extends Language

  case object DE_EN extends Language

}
