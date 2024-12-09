package database.tables

import database.UUIDUniqueColumn
import models.CourseId
import models.Module
import slick.jdbc.PostgresProfile.api._

final class ModuleTable(tag: Tag) extends Table[Module](tag, "module") with UUIDUniqueColumn {

  def label = column[String]("label")

  def abbrev = column[String]("abbrev")

  def language = column[String]("language")

  def season = column[String]("season")

  def parts = column[List[CourseId]]("parts")

  def * = (
    id,
    label,
    abbrev,
    language,
    season,
    parts
  ) <> (Module.apply, Module.unapply)
}
