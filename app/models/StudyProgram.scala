package models

import database.tables.StudyProgramDBEntry
import json.JsonNullWritable
import play.api.libs.json.{Json, Writes}

import java.util.UUID

sealed trait StudyProgram extends UniqueEntity {
  def teachingUnitId: UUID

  def graduationId: UUID

  def label: String

  def abbreviation: String

  def parentId: Option[UUID]
}

object StudyProgram {
  case class StudyProgramDefault(
      teachingUnit: UUID,
      graduation: UUID,
      label: String,
      abbreviation: String,
      parent: Option[UUID],
      id: UUID
  ) extends StudyProgram {
    override def teachingUnitId = teachingUnit

    override def graduationId = graduation

    override def parentId = parent
  }

  case class StudyProgramAtom(
      teachingUnit: TeachingUnit,
      graduation: Graduation,
      label: String,
      abbreviation: String,
      parent: Option[StudyProgramAtom],
      id: UUID
  ) extends StudyProgram {
    override def teachingUnitId = teachingUnit.id

    override def graduationId = graduation.id

    override def parentId = parent.map(_.id)
  }

  def apply(db: StudyProgramDBEntry): StudyProgramDefault =
    StudyProgramDefault(
      db.teachingUnit,
      db.graduation,
      db.label,
      db.abbreviation,
      db.parent,
      db.id
    )

  /*  def apply(
      sp: (
          StudyProgramDBEntry,
          TeachingUnitDbEntry,
          GraduationDbEntry,
          Option[StudyProgramDBEntry]
      )
  ): StudyProgramAtom = StudyProgramAtom(
    TeachingUnit(sp._2),
    Graduation(sp._3),
    sp._1.label,
    sp._1.abbreviation,
    sp._4.map(apply),
    sp._1.id
  )*/
}
