package controllers

import json.RoomFormat
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
    with Controller[RoomJson, Room]
    with RoomFormat {
  override protected implicit val writes: Writes[Room] =
    roomWrites

  override protected implicit val reads: Reads[RoomJson] =
    roomJsonFmt
}
