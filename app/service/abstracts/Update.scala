package service.abstracts

import models.UniqueEntity

import java.util.UUID
import scala.concurrent.Future

trait Update[Json, Model <: UniqueEntity] {
  self: JsonConverter[Json, Model] with Core[Model, _] =>

  protected def canUpdate(json: Json, existing: Model): Boolean

  def update(json: Json, id: UUID): Future[Model] =
    repo.update(
      toModel(json, Some(id)),
      canUpdate(json, _)
    )
}
