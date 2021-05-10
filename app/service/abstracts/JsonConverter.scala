package service.abstracts

import database.UniqueDbEntry

import java.util.UUID

trait JsonConverter[Json, DbEntry <: UniqueDbEntry] {
  protected def toUniqueDbEntry(json: Json, id: Option[UUID]): DbEntry
}
