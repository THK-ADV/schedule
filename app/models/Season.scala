package models

import play.api.libs.json.{JsResult, JsString, Reads, Writes}

import scala.util.{Failure, Success, Try}

sealed trait Season

object Season {

  case object SoSe extends Season

  case object WiSe extends Season

  case object SoSe_WiSe extends Season

  implicit val writes: Writes[Season] =
    Writes[Season](unapply _ andThen JsString)

  implicit val reads: Reads[Season] = Reads(
    _.validate[String].flatMap(s => JsResult.fromTry(apply(s)))
  )

  def apply(string: String): Try[Season] = string match {
    case "SoSe"                    => Success(SoSe)
    case "WiSe"                    => Success(WiSe)
    case "SoSe_WiSe" | "WiSe_SoSe" => Success(SoSe_WiSe)
    case _ =>
      Failure(
        new Throwable(s"expected SoSe, WiSe or SoSe_WiSe, but was $string")
      )
  }

  def unapply(lang: Season): String = lang match {
    case SoSe      => "SoSe"
    case WiSe      => "WiSe"
    case SoSe_WiSe => "SoSe_WiSe"
  }
}
