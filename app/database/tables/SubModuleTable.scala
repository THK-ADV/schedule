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

  def language = column[Int]("language")

  def season = column[Int]("season")

  def hasLanguage(l: Language): Rep[Boolean] =
    this.language === language(l)

  def hasSeason(s: Season): Rep[Boolean] =
    this.season === season(s)

  def language(int: Int): Language = int match {
    case 0 => Language.DE
    case 1 => Language.EN
    case 2 => Language.DE_EN
  }

  def language(lang: Language): Int = lang match {
    case Language.DE    => 0
    case Language.EN    => 1
    case Language.DE_EN => 2
  }

  def season(int: Int): Season = int match {
    case 0 => Season.WiSe
    case 1 => Season.SoSe
    case 2 => Season.SoSe_WiSe
  }

  def season(season: Season): Int = season match {
    case Season.WiSe      => 0
    case Season.SoSe      => 1
    case Season.SoSe_WiSe => 2
  }

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
      (UUID, String, String, Int, Double, String, Int, Int, Timestamp, UUID)
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
        this.language(language),
        this.season(season),
        lastModified,
        id
      )
  }

  def unmapRow: SubModuleDbEntry => Option[
    (UUID, String, String, Int, Double, String, Int, Int, Timestamp, UUID)
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
          language(a.language),
          season(a.season),
          a.lastModified,
          a.id
        )
      )
}
