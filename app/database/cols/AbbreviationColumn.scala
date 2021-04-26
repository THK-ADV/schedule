package database.cols

import slick.jdbc.PostgresProfile.api._

trait AbbreviationColumn {
  self: Table[_] =>
  def abbreviation = column[String]("abbreviation")

  def hasAbbreviation(abbrev: String): Rep[Boolean] =
    abbreviation.toLowerCase === abbrev.toLowerCase

  def likeAbbreviation(abbrev: String): Rep[Boolean] =
    abbreviation.toLowerCase like s"%${abbrev.toLowerCase}"
}
