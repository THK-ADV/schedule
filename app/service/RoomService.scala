package service

import database.repos.RoomRepository
import database.tables.RoomTable
import models.{Room, RoomJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class RoomService @Inject() (val repo: RoomRepository)
    extends Service[RoomJson, Room, RoomTable] {

  override protected def toModel(json: RoomJson, id: Option[UUID]) =
    Room(
      json.label,
      json.number,
      json.seats,
      json.roomType,
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: RoomJson,
      existing: Room
  ): Boolean =
    json.number == existing.number

  override protected def uniqueCols(json: RoomJson, table: RoomTable) =
    List(table.hasNumber(json.number))

  override protected def validate(json: RoomJson) = None
}
