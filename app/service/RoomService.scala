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
      json.label,
      json.abbreviation,
      now(),
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: RoomJson,
      existing: RoomDbEntry
  ): Boolean =
    json.abbreviation == existing.abbreviation

  override protected def uniqueCols(json: RoomJson, table: RoomTable) =
    List(table.hasAbbreviation(json.abbreviation))

  override protected def validate(json: RoomJson) = None
}
