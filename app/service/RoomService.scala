package service

import database.repos.RoomRepository
import database.tables.{RoomDbEntry, RoomTable}
import models.{Room, RoomJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class RoomService @Inject() (val repo: RoomRepository)
    extends Service[RoomJson, Room, RoomDbEntry, RoomTable] {

  override protected def toUniqueDbEntry(json: RoomJson, id: Option[UUID]) =
    RoomDbEntry(
      json.campus,
      json.label,
      json.abbreviation,
      now(),
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: RoomJson,
      existing: RoomDbEntry
  ): Boolean =
    json.abbreviation == existing.abbreviation &&
      json.campus == existing.campus

  override protected def uniqueCols(json: RoomJson) =
    List(
      _.hasAbbreviation(json.abbreviation),
      _.campus(json.campus)
    )

  override protected def validate(json: RoomJson) = None
}
