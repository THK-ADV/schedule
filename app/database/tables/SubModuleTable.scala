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
    with DescriptionUrlColumn {

  def module = column[UUID]("module")

  def recommendedSemester = column[Int]("recommended_semester")

  def hasModule(id: UUID) = module === id

  def * = (
    module,
    label,
    abbreviation,
    recommendedSemester,
    credits,
    descriptionUrl,
    id
  ) <> (mapRow, unmapRow)

  def mapRow
      : ((UUID, String, String, Int, Double, String, UUID)) => SubModule = {
    case (module, label, abbreviation, semester, credits, url, id) =>
      SubModule(module, label, abbreviation, semester, credits, url, id)
  }

  def unmapRow
      : SubModule => Option[(UUID, String, String, Int, Double, String, UUID)] =
    a =>
      Option(
        (
          a.module,
          a.label,
          a.abbreviation,
          a.recommendedSemester,
          a.credits,
          a.descriptionUrl,
          a.id
        )
      )
}
