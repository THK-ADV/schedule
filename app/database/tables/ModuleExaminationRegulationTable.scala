package database.tables

import database.UniqueDbEntry
import database.cols.{
  ExaminationRegulationColumn,
  ModuleColumn,
  UniqueEntityColumn
}
import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp
import java.util.UUID

case class ModuleExaminationRegulationDbEntry(
    module: UUID,
    examinationRegulation: UUID,
    mandatory: Boolean,
    lastModified: Timestamp,
    id: UUID
) extends UniqueDbEntry

class ModuleExaminationRegulationTable(tag: Tag)
    extends Table[ModuleExaminationRegulationDbEntry](
      tag,
      "module_examination_regulation"
    )
    with UniqueEntityColumn
    with ModuleColumn
    with ExaminationRegulationColumn {

  def mandatory = column[Boolean]("mandatory")

  def isMandatory(mandatory: Boolean) = this.mandatory === mandatory

  def * = (
    module,
    examinationRegulation,
    mandatory,
    lastModified,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: (
      (UUID, UUID, Boolean, Timestamp, UUID)
  ) => ModuleExaminationRegulationDbEntry = {
    case (module, examinationRegulation, mandatory, lastModified, id) =>
      ModuleExaminationRegulationDbEntry(
        module,
        examinationRegulation,
        mandatory,
        lastModified,
        id
      )
  }

  def unmapRow: ModuleExaminationRegulationDbEntry => Option[
    (UUID, UUID, Boolean, Timestamp, UUID)
  ] =
    a =>
      Option(
        (a.module, a.examinationRegulation, a.mandatory, a.lastModified, a.id)
      )
}
