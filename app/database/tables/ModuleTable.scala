package database.tables

import database.cols._
import models.Module
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

class ModuleTable(tag: Tag)
    extends Table[Module](tag, "module")
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
    id
  ) <> (mapRow, unmapRow)

  def mapRow: ((UUID, UUID, String, String, Double, String, UUID)) => Module = {
    case (
          examinationRegulation,
          courseManager,
          label,
          abbreviation,
          credits,
          url,
          id
        ) =>
      Module(
        examinationRegulation,
        courseManager,
        label,
        abbreviation,
        credits,
        url,
        id
      )
  }

  def unmapRow
      : Module => Option[(UUID, UUID, String, String, Double, String, UUID)] =
    a =>
      Option(
        (
          a.examinationRegulation,
          a.courseManager,
          a.label,
          a.abbreviation,
          a.credits,
          a.descriptionUrl,
          a.id
        )
      )
}
