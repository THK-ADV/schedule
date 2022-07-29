package models

import database.tables.SubModuleDbEntry

import java.util.UUID

sealed trait SubModule extends UniqueEntity {
  def moduleId: UUID

  def label: String

  def abbreviation: String

  def recommendedSemester: Int

  def credits: Double

  def descriptionUrl: String

  def language: Language

  def season: Season
}

object SubModule {
  case class SubModuleDefault(
      module: UUID,
      label: String,
      abbreviation: String,
      recommendedSemester: Int,
      credits: Double,
      descriptionUrl: String,
      language: Language,
      season: Season,
      id: UUID
  ) extends SubModule {
    override def moduleId = module
  }

  case class SubModuleAtom(
      module: Module,
      label: String,
      abbreviation: String,
      recommendedSemester: Int,
      credits: Double,
      descriptionUrl: String,
      language: Language,
      season: Season,
      id: UUID
  ) extends SubModule {
    override def moduleId = module.id
  }

  def apply(db: SubModuleDbEntry): SubModuleDefault =
    SubModuleDefault(
      db.module,
      db.label,
      db.abbreviation,
      db.recommendedSemester,
      db.credits,
      db.descriptionUrl,
      db.language,
      db.season,
      db.id
    )
}
