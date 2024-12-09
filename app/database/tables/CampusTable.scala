package database.tables

import database.UUIDUniqueColumn
import models.Campus
import slick.jdbc.PostgresProfile.api._

final class CampusTable(tag: Tag) extends Table[Campus](tag, "campus") with UUIDUniqueColumn {

  def label = column[String]("label")

  def abbrev = column[String]("abbrev")

  def * = (id, label, abbrev) <> (Campus.apply, Campus.unapply)
}
