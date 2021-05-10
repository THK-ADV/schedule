package database.tables

import database.UniqueDbEntry
import database.cols._
import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp
import java.util.UUID

case class ModuleDbEntry(
    examinationRegulation: UUID,
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
    with ExaminationRegulationColumn
    with UserColumn {

  override protected def userColumnName = "course_manager"

  def * = (
    examinationRegulation,
    user,
    label,
    abbreviation,
    credits,
    descriptionUrl,
    lastModified,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: (
      (UUID, UUID, String, String, Double, String, Timestamp, UUID)
  ) => ModuleDbEntry = {
    case (
          examinationRegulation,
          courseManager,
          label,
          abbreviation,
          credits,
          url,
          lastModified: Timestamp,
          id
        ) =>
      ModuleDbEntry(
        examinationRegulation,
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
    (UUID, UUID, String, String, Double, String, Timestamp, UUID)
  ] =
    a =>
      Option(
        (
          a.examinationRegulation,
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
