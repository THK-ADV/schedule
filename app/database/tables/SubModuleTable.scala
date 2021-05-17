package database.tables

import database.UniqueDbEntry
import database.cols._
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

  def * = (
    module,
    label,
    abbreviation,
    recommendedSemester,
    credits,
    descriptionUrl,
    lastModified,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: (
      (UUID, String, String, Int, Double, String, Timestamp, UUID)
  ) => SubModuleDbEntry = {
    case (
          module,
          label,
          abbreviation,
          semester,
          credits,
          url,
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
        lastModified,
        id
      )
  }

  def unmapRow: SubModuleDbEntry => Option[
    (UUID, String, String, Int, Double, String, Timestamp, UUID)
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
          a.lastModified,
          a.id
        )
      )
}
