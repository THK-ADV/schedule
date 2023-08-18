package controllers

import controllers.crud.{JsonHttpResponse, Read}
import json.RoomWrites
import models.Room
import play.api.mvc.{AbstractController, ControllerComponents}
import service.RoomService

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class RoomController @Inject() (
    cc: ControllerComponents,
    val service: RoomService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Read[UUID, Room]
    with RoomWrites
    with JsonHttpResponse[Room]
