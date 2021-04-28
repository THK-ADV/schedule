package database.tables

import database.cols._
import models.Module
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

class ModuleTable(tag: Tag)
    extends Table[Module](tag, "module")
    with IDColumn
    with AbbreviationColumn
    with LabelColumn
    with CreditsColumn
    with DescriptionUrlColumn
    with ExaminationRegulationColumn {

  def * = (
    examinationRegulation,
    label,
    abbreviation,
    credits,
    descriptionUrl,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: ((UUID, String, String, Double, String, UUID)) => Module = {
    case (examinationRegulation, label, abbreviation, credits, url, id) =>
      Module(examinationRegulation, label, abbreviation, credits, url, id)
  }

  def unmapRow: Module => Option[(UUID, String, String, Double, String, UUID)] =
    a =>
      Option(
        (
          a.examinationRegulation,
          a.label,
          a.abbreviation,
          a.credits,
          a.descriptionUrl,
          a.id
        )
      )
}
