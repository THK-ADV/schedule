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
      json.label,
      json.abbreviation,
      json.graduation,
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: StudyProgramJson,
      existing: StudyProgram
  ): Boolean =
    json.label == existing.label && json.graduation == existing.graduation

  override protected def uniqueCols(
      json: StudyProgramJson,
      table: StudyProgramTable
  ) = List(table.hasLabel(json.label), table.hasGraduation(json.graduation))

  override protected def validate(json: StudyProgramJson): Option[Throwable] =
    Option.unless(json.graduation == "BA" || json.graduation == "MA")(
      new Throwable("graduation must be either 'BA' or 'MA'")
    )
}
