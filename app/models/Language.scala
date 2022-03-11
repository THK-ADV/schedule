package models

import play.api.libs.json._

sealed trait Language {
  override def toString = Language.unapply(this)
}

object Language {
  implicit val format: Format[Language] =
    Format.of[String].bimap(Language.apply, Language.unapply)

  def apply(string: String): Language = string.toLowerCase match {
    case "en"              => EN
    case "de"              => DE
    case "de_en" | "en_de" => DE_EN
    case _                 => Unknown
  }

  def unapply(lang: Language): String = lang match {
    case EN      => "en"
    case DE      => "de"
    case DE_EN   => "de_en"
    case Unknown => "unknown"
  }

  case object EN extends Language

  case object DE extends Language

  case object DE_EN extends Language

  case object Unknown extends Language

}
