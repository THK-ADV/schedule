package database.tables

import database.UniqueDbEntry
import database.cols._
import models.{Language, Season}
import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp
import java.util.UUID

case class SubModuleDbEntry(
    module: UUID,
    label: String,
    abbreviation: String,
    recommendedSemester: Int,
    credits: Double,
    descriptionUrl: String,
    language: Language,
    season: Season,
    lastModified: Timestamp,
    id: UUID
) extends UniqueDbEntry

class SubModuleTable(tag: Tag)
    extends Table[SubModuleDbEntry](tag, "submodule")
    with UniqueEntityColumn
    with AbbreviationColumn
    with LabelColumn
    with CreditsColumn
    with DescriptionUrlColumn
    with ModuleColumn {

  def recommendedSemester = column[Int]("recommended_semester")

  def language = column[String]("language")

  def season = column[String]("season")

  def hasLanguage(l: Language): Rep[Boolean] =
    this.language === l.toString

  def hasSeason(s: Season): Rep[Boolean] =
    this.season === s.toString

  def * = (
    module,
    label,
    abbreviation,
    recommendedSemester,
    credits,
    descriptionUrl,
    language,
    season,
    lastModified,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: (
      (
          UUID,
          String,
          String,
          Int,
          Double,
          String,
          String,
          String,
          Timestamp,
          UUID
      )
  ) => SubModuleDbEntry = {
    case (
          module,
          label,
          abbreviation,
          semester,
          credits,
          url,
          language,
          season,
          lastModified,
          id
        ) =>
      SubModuleDbEntry(
        module,
        label,
        abbreviation,
        semester,
        credits,
        url,
        Language(language),
        Season(season),
        lastModified,
        id
      )
  }

  def unmapRow: SubModuleDbEntry => Option[
    (UUID, String, String, Int, Double, String, String, String, Timestamp, UUID)
  ] =
    a =>
      Option(
        (
          a.module,
          a.label,
          a.abbreviation,
          a.recommendedSemester,
          a.credits,
          a.descriptionUrl,
          a.language.toString,
          a.season.toString,
          a.lastModified,
          a.id
        )
      )
}
