package database.tables

import database.cols.{LocalizedLabelColumn, StringUniqueColumn}
import models.Season
import slick.jdbc.PostgresProfile.api._

final class SeasonTable(tag: Tag)
    extends Table[Season](tag, "season")
    with StringUniqueColumn
    with LocalizedLabelColumn {

  def * = (
    id,
    deLabel,
    enLabel
  ) <> (Season.tupled, Season.unapply)
}
