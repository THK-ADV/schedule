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
    mandatory: Boolean,
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

  def mandatory = column[Boolean]("mandatory")

  def recommendedSemester = column[Int]("recommended_semester")

  def * = (
    module,
    label,
    abbreviation,
    mandatory,
    recommendedSemester,
    credits,
    descriptionUrl,
    lastModified,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: (
      (UUID, String, String, Boolean, Int, Double, String, Timestamp, UUID)
  ) => SubModuleDbEntry = {
    case (
          module,
          label,
          abbreviation,
          mandatory,
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
        mandatory,
        semester,
        credits,
        url,
        lastModified,
        id
      )
  }

  def unmapRow: SubModuleDbEntry => Option[
    (UUID, String, String, Boolean, Int, Double, String, Timestamp, UUID)
  ] =
    a =>
      Option(
        (
          a.module,
          a.label,
          a.abbreviation,
          a.mandatory,
          a.recommendedSemester,
          a.credits,
          a.descriptionUrl,
          a.lastModified,
          a.id
        )
      )
}
