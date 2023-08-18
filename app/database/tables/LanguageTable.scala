package database.tables

import database.cols.{LocalizedLabelColumn, StringUniqueColumn}
import models.Language
import slick.jdbc.PostgresProfile.api._

final class LanguageTable(tag: Tag)
    extends Table[Language](tag, "language")
    with StringUniqueColumn
    with LocalizedLabelColumn {

  def * = (
    id,
    deLabel,
    enLabel
  ) <> (Language.tupled, Language.unapply)
}
