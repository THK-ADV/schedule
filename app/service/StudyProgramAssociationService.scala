package service

import database.repos.StudyProgramAssociationRepository
import database.tables.StudyProgramAssociationTable
import models.{StudyProgramAssociation, StudyProgramAssociationJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class StudyProgramAssociationService @Inject() (
    val repo: StudyProgramAssociationRepository
) extends Service[
      StudyProgramAssociationJson,
      StudyProgramAssociation,
      StudyProgramAssociationTable
    ] {

  override protected def toModel(
      json: StudyProgramAssociationJson,
      id: Option[UUID]
  ) = StudyProgramAssociation(
    json.teachingUnit,
    json.studyProgram,
    id getOrElse UUID.randomUUID
  )

  override protected def canUpdate(
      json: StudyProgramAssociationJson,
      existing: StudyProgramAssociation
  ): Boolean = true

  override protected def uniqueCols(
      json: StudyProgramAssociationJson,
      table: StudyProgramAssociationTable
  ) = List.empty

  override protected def validate(json: StudyProgramAssociationJson) = None
}
