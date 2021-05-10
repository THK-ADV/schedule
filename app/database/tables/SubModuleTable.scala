package database.tables

import database.cols._
import models.SubModule
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

class SubModuleTable(tag: Tag)
    extends Table[SubModule](tag, "submodule")
    with IDColumn
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
    id
  ) <> (mapRow, unmapRow)

  def mapRow: (
      (UUID, String, String, Boolean, Int, Double, String, UUID)
  ) => SubModule = {
    case (module, label, abbreviation, mandatory, semester, credits, url, id) =>
      SubModule(
        module,
        label,
        abbreviation,
        mandatory,
        semester,
        credits,
        url,
        id
      )
  }

  def unmapRow: SubModule => Option[
    (UUID, String, String, Boolean, Int, Double, String, UUID)
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
          a.id
        )
      )
}
