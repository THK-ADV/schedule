package service

import database.repos.StudyProgramRepository
import database.tables.{StudyProgramDBEntry, StudyProgramTable}
import models.{StudyProgram, StudyProgramJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class StudyProgramService @Inject() (val repo: StudyProgramRepository)
    extends Service[
      StudyProgramJson,
      StudyProgram,
      StudyProgramDBEntry,
      StudyProgramTable
    ] {

  override protected def toUniqueDbEntry(
      json: StudyProgramJson,
      id: Option[UUID]
  ) =
    StudyProgramDBEntry(
      json.teachingUnit,
      json.graduation,
      json.label,
      json.abbreviation,
      now(),
      id = id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: StudyProgramJson,
      existing: StudyProgramDBEntry
  ): Boolean = true

  override protected def uniqueCols(json: StudyProgramJson) = List.empty

  override protected def validate(json: StudyProgramJson) = None
}
