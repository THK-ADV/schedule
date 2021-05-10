package service.abstracts

import database.UniqueDbEntry

import java.sql.Timestamp
import java.util.UUID

trait JsonConverter[Json, DbEntry <: UniqueDbEntry] {
  protected def toUniqueDbEntry(json: Json, id: Option[UUID]): DbEntry

  protected final def now(): Timestamp =
    new Timestamp(System.currentTimeMillis())
}
