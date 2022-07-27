package models

import database.tables.ModuleDbEntry

import java.util.UUID

sealed trait Module extends UniqueEntity {
  def courseManagerId: UUID

  def label: String

  def abbreviation: String

  def credits: Double

  def descriptionUrl: String
}

object Module {
  case class ModuleDefault(
      courseManager: UUID,
      label: String,
      abbreviation: String,
      credits: Double,
      descriptionUrl: String,
      id: UUID
  ) extends Module {
    override def courseManagerId = courseManager
  }

  case class ModuleAtom(
      courseManager: User,
      label: String,
      abbreviation: String,
      credits: Double,
      descriptionUrl: String,
      id: UUID
  ) extends Module {
    override def courseManagerId = courseManager.id
  }

  def apply(db: ModuleDbEntry): ModuleDefault =
    ModuleDefault(
      db.courseManager,
      db.label,
      db.abbreviation,
      db.credits,
      db.descriptionUrl,
      db.id
    )
}
