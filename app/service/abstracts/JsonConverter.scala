package service.abstracts

import java.util.UUID

trait JsonConverter[Json, Model] {
  protected def toModel(json: Json, id: Option[UUID]): Model
}
