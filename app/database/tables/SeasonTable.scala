package database.tables

import database.StringUniqueColumn
import models.Season
import slick.jdbc.PostgresProfile.api._

final class SeasonTable(tag: Tag) extends Table[Season](tag, "season") with StringUniqueColumn {

  def deLabel = column[String]("de_label")

  def enLabel = column[String]("en_label")

  def * = (
    id,
    deLabel,
    enLabel
  ) <> (Season.apply, Season.unapply)
}
