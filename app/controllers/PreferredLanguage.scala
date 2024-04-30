package controllers

case class PreferredLanguage(value: String) extends AnyVal

object PreferredLanguage {
  val Default = PreferredLanguage("de")
}
