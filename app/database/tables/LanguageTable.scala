package database.tables

import database.StringUniqueColumn
import models.Language
import slick.jdbc.PostgresProfile.api._

final class LanguageTable(tag: Tag) extends Table[Language](tag, "language") with StringUniqueColumn {

  def deLabel = column[String]("de_label")

  def enLabel = column[String]("en_label")

  def * = (
    id,
    deLabel,
    enLabel
  ) <> (Language.apply, Language.unapply)
}
