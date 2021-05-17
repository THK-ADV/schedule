package service

import database.repos.TeachingUnitAssociationRepository
import database.tables.{
  TeachingUnitAssociationDbEntry,
  TeachingUnitAssociationTable
}
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
      TeachingUnitAssociationDbEntry,
      TeachingUnitAssociationTable
    ] {

  override protected def toUniqueDbEntry(
      json: TeachingUnitAssociationJson,
      id: Option[UUID]
  ) =
    TeachingUnitAssociationDbEntry(
      json.faculty,
      json.teachingUnit,
      now(),
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: TeachingUnitAssociationJson,
      existing: TeachingUnitAssociationDbEntry
  ): Boolean = true

  override protected def uniqueCols(json: TeachingUnitAssociationJson) = Nil

  override protected def validate(json: TeachingUnitAssociationJson) = None
}
