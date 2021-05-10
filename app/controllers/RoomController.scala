/*
package controllers

import models.{Room, RoomJson}
import play.api.libs.json.{Reads, Writes}
import play.api.mvc.{AbstractController, ControllerComponents}
import service.RoomService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

// TODO this might by obsolete when we switch to reservations
@Singleton
class RoomController @Inject() (
    cc: ControllerComponents,
    val service: RoomService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Controller[RoomJson, Room] {
  override protected implicit def writes: Writes[Room] = Room.format

  override protected implicit def reads: Reads[RoomJson] = RoomJson.format
}
*/
