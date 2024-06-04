package controllers

import controllers.PreferredLanguage.Default

case class PreferredLanguage(value: String) extends AnyVal {
  def isDe = value == Default.value
}

object PreferredLanguage {
  val Default = PreferredLanguage("de")
}
