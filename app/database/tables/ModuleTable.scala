package database.tables

import database.UniqueDbEntry
import database.cols._
import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp
import java.util.UUID

case class ModuleDbEntry(
    courseManager: UUID,
    label: String,
    abbreviation: String,
    credits: Double,
    descriptionUrl: String,
    lastModified: Timestamp,
    id: UUID
) extends UniqueDbEntry

class ModuleTable(tag: Tag)
    extends Table[ModuleDbEntry](tag, "module")
    with UniqueEntityColumn
    with AbbreviationColumn
    with LabelColumn
    with CreditsColumn
    with DescriptionUrlColumn
    with UserColumn {

  override protected def userColumnName = "course_manager"

  def * = (
    user,
    label,
    abbreviation,
    credits,
    descriptionUrl,
    lastModified,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: (
      (UUID, String, String, Double, String, Timestamp, UUID)
  ) => ModuleDbEntry = {
    case (
          courseManager,
          label,
          abbreviation,
          credits,
          url,
          lastModified: Timestamp,
          id
        ) =>
      ModuleDbEntry(
        courseManager,
        label,
        abbreviation,
        credits,
        url,
        lastModified: Timestamp,
        id
      )
  }

  def unmapRow: ModuleDbEntry => Option[
    (UUID, String, String, Double, String, Timestamp, UUID)
  ] =
    a =>
      Option(
        (
          a.courseManager,
          a.label,
          a.abbreviation,
          a.credits,
          a.descriptionUrl,
          a.lastModified: Timestamp,
          a.id
        )
      )
}
