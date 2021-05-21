package models

import play.api.libs.json.{JsString, Reads, Writes}

sealed trait Season {
  override def toString = Season.unapply(this)
}

object Season {

  case object SoSe extends Season

  case object WiSe extends Season

  case object SoSe_WiSe extends Season

  case object Unknown extends Season

  implicit val writes: Writes[Season] =
    Writes[Season](unapply _ andThen JsString)

  implicit val reads: Reads[Season] =
    Reads(_.validate[String].map(apply))

  def apply(string: String): Season = string match {
    case "SoSe"                    => SoSe
    case "WiSe"                    => WiSe
    case "SoSe_WiSe" | "WiSe_SoSe" => SoSe_WiSe
    case _                         => Unknown
  }

  def unapply(lang: Season): String = lang match {
    case SoSe      => "SoSe"
    case WiSe      => "WiSe"
    case SoSe_WiSe => "SoSe_WiSe"
    case Unknown   => "unknown"
  }
}
