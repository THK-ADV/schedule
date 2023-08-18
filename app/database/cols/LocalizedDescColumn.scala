package database.cols

import localization.Localization
import slick.jdbc.PostgresProfile.api._

trait LocalizedDescColumn {
  self: Table[_] =>
  def deDesc = column[String]("de_desc")

  def enDesc = column[String]("en_desc")

  def hasDesc(desc: String)(implicit l: Localization): Rep[Boolean] =
    l.fold(this.deDesc, this.enDesc).toLowerCase === desc.toLowerCase
}
