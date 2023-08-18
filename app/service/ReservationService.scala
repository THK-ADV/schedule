package service

import database.repos.ReservationRepository
import models.Reservation
import service.abstracts.{Create, Get}

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class ReservationService @Inject() (
    val repo: ReservationRepository,
    implicit val ctx: ExecutionContext
) extends Get[UUID, Reservation]
    with Create[Reservation]
