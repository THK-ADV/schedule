/*
package service

import database.tables.RoomTable
import models.{Room, RoomJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}


@Singleton
class RoomService @Inject() (val repo: RoomRepository)
    extends Service[RoomJson, Room, RoomTable] {

  override protected def toUniqueDbEntry(json: RoomJson, id: Option[UUID]) =
    Room(
      json.label,
      json.abbreviation,
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: RoomJson,
      existing: Room
  ): Boolean =
    json.abbreviation == existing.abbreviation

  override protected def uniqueCols(json: RoomJson, table: RoomTable) =
    List(table.hasAbbreviation(json.abbreviation))

  override protected def validate(json: RoomJson) = None
}*/
