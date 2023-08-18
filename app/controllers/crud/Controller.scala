//package controllers.crud
//
//import controllers.JsonHttpResponse
//import models.UniqueEntity
//import play.api.libs.json.Reads
//import play.api.mvc.AbstractController
//import service.abstracts.Service
//
//import java.util.UUID
//
//trait Controller[Json, Model <: UniqueEntity] extends JsonHttpResponse[Model] {
//  self: AbstractController =>
//
//  protected def service: Service[Json, Model, _, _]
//
//  protected implicit def reads: Reads[Json]
//
//  def all() = Action.async { r =>
//    okSeq(service.all(r.queryString, parseAtomic(r.queryString)))
//  }
//
//  def get(id: UUID) = Action.async { r =>
//    okOpt(service.get(id, parseAtomic(r.queryString)))
//  }
//
//  def delete(id: UUID) = Action.async {
//    ok(service.delete(id))
//  }
//
//  def create() = Action.async(parse.json[Json]) { r =>
//    created(service.create(r.body))
//  }
//
//  def update(id: UUID) = Action.async(parse.json[Json]) { r =>
//    ok(service.update(r.body, id))
//  }
//
//  def parseAtomic(f: Map[String, Seq[String]]): Boolean =
//    f.get("atomic")
//      .flatMap(_.headOption)
//      .flatMap(_.toBooleanOption)
//      .getOrElse(false)
//}
