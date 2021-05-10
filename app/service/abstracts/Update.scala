package service.abstracts

import database.UniqueDbEntry
import models.UniqueEntity

import java.util.UUID
import scala.concurrent.Future

trait Update[Json, Model <: UniqueEntity, DbEntry <: UniqueDbEntry] {
  self: JsonConverter[Json, DbEntry] with Core[Model, DbEntry, _] =>

  protected def canUpdate(json: Json, existing: DbEntry): Boolean

  def update(json: Json, id: UUID): Future[Model] =
    repo.update(
      toUniqueDbEntry(json, Some(id)),
      canUpdate(json, _)
    )
}
