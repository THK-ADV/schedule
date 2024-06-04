package service

import database.repos.RoomRepository
import models.Room
import service.abstracts.{Create, Get}

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class RoomService @Inject() (
    val repo: RoomRepository,
    implicit val ctx: ExecutionContext
) extends Get[UUID, Room]
    with Create[Room]
