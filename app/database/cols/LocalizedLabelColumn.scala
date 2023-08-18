package database.cols

import localization.Localization
import slick.jdbc.PostgresProfile.api._

trait LocalizedLabelColumn {
  self: Table[_] =>
  def deLabel = column[String]("de_label")

  def enLabel = column[String]("en_label")

  def hasLabel(label: String)(implicit l: Localization): Rep[Boolean] =
    l.fold(this.deLabel, this.enLabel).toLowerCase === label.toLowerCase
}
