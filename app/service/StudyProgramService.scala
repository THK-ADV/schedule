package service

import database.repos.StudyProgramRepository
import database.tables.StudyProgramTable
import models.{StudyProgram, StudyProgramJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class StudyProgramService @Inject() (val repo: StudyProgramRepository)
    extends Service[StudyProgramJson, StudyProgram, StudyProgramTable] {

  override protected def toModel(json: StudyProgramJson, id: Option[UUID]) =
    StudyProgram(
      json.teachingUnit,
      json.graduation,
      json.label,
      json.abbreviation,
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: StudyProgramJson,
      existing: StudyProgram
  ): Boolean = {
    json.teachingUnit == existing.teachingUnit &&
    json.label == existing.label &&
    json.graduation == existing.graduation
  }

  override protected def uniqueCols(
      json: StudyProgramJson,
      table: StudyProgramTable
  ) = List(
    table.hasTeachingUnit(json.teachingUnit),
    table.hasLabel(json.label),
    table.hasGraduation(json.graduation)
  )

  override protected def validate(json: StudyProgramJson) = None
}
