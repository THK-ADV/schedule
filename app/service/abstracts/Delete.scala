//package service.abstracts
//
//import database.UniqueDbEntry
//import models.UniqueEntity
//
//import java.util.UUID
//import scala.concurrent.Future
//
//trait Delete[Model <: UniqueEntity, DbEntry <: UniqueDbEntry] {
//  self: Core[Model, DbEntry, _] =>
//
//  def delete(id: UUID): Future[Model] =
//    repo.delete(id)
//}
