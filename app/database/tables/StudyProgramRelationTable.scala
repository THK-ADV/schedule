package database.tables

import models.StudyProgramRelation
import slick.jdbc.PostgresProfile.api._

final class StudyProgramRelationTable(tag: Tag)
    extends Table[StudyProgramRelation](tag, "study_program_relation") {

  def parent = column[String]("parent", O.PrimaryKey)

  def child = column[String]("child", O.PrimaryKey)

  def parentFk =
    foreignKey("parent", parent, TableQuery[StudyProgramTable])(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )

  def childFk =
    foreignKey("child", child, TableQuery[StudyProgramTable])(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )

  def * = (
    parent,
    child
  ) <> (StudyProgramRelation.tupled, StudyProgramRelation.unapply)
}
