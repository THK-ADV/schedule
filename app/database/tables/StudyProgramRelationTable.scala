package database.tables

import models.StudyProgramRelation
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

final class StudyProgramRelationTable(tag: Tag)
    extends Table[StudyProgramRelation](tag, "study_program_relation") {

  def parent = column[UUID]("parent", O.PrimaryKey)

  def child = column[UUID]("child", O.PrimaryKey)

  def * = (
    parent,
    child
  ) <> (StudyProgramRelation.tupled, StudyProgramRelation.unapply)
}
