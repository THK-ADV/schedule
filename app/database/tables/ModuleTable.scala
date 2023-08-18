package database.tables

import database.cols._
import models.{Module, ModulePart, ModuleType}
import slick.jdbc.PostgresProfile.api._

final class ModuleTable(tag: Tag)
    extends Table[Module](tag, "module")
    with UUIDUniqueColumn {

  def label = column[String]("label")

  def abbrev = column[String]("abbrev")

  def language = column[String]("language")

  def season = column[String]("season")

  def moduleType = column[ModuleType]("type")

  def active = column[Boolean]("active")

  def parts = column[List[ModulePart]]("parts")

  def * = (
    id,
    label,
    abbrev,
    language,
    season,
    moduleType,
    active,
    parts
  ) <> (Module.tupled, Module.unapply)
}
