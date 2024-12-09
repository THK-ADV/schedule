package service

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext

import database.repos.RoomRepository
import models.Room
import service.abstracts.Create
import service.abstracts.Get

@Singleton
final class RoomService @Inject() (
    val repo: RoomRepository,
    implicit val ctx: ExecutionContext
) extends Get[UUID, Room]
    with Create[UUID, Room]
