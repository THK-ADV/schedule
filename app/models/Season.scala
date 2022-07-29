package models

sealed trait Season {
  override def toString = Season.unapply(this)
}

object Season {

  case object SoSe extends Season

  case object WiSe extends Season

  case object SoSe_WiSe extends Season

  case object Unknown extends Season

  def apply(string: String): Season = string.toLowerCase match {
    case "sose"                    => SoSe
    case "wise"                    => WiSe
    case "sose_wise" | "wise_sose" => SoSe_WiSe
    case _                         => Unknown
  }

  def unapply(lang: Season): String = lang match {
    case SoSe      => "SoSe"
    case WiSe      => "WiSe"
    case SoSe_WiSe => "SoSe_WiSe"
    case Unknown   => "unknown"
  }
}
