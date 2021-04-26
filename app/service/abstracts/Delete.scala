package service.abstracts

import models.UniqueEntity

import java.util.UUID
import scala.concurrent.Future

trait Delete[Model <: UniqueEntity] {
  self: Core[Model, _] =>

  def delete(id: UUID): Future[Model] =
    repo.delete(id)
}
