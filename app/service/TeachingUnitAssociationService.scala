package service

import database.repos.TeachingUnitAssociationRepository
import database.tables.TeachingUnitAssociationTable
import models.{TeachingUnitAssociation, TeachingUnitAssociationJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class TeachingUnitAssociationService @Inject() (
    val repo: TeachingUnitAssociationRepository
) extends Service[
      TeachingUnitAssociationJson,
      TeachingUnitAssociation,
      TeachingUnitAssociationTable
    ] {

  override protected def toModel(
      json: TeachingUnitAssociationJson,
      id: Option[UUID]
  ) =
    TeachingUnitAssociation(
      json.faculty,
      json.teachingUnit,
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: TeachingUnitAssociationJson,
      existing: TeachingUnitAssociation
  ): Boolean = true

  override protected def uniqueCols(
      json: TeachingUnitAssociationJson,
      table: TeachingUnitAssociationTable
  ) = List.empty
}
