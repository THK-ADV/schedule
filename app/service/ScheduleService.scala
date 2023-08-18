package service

import database.repos.ScheduleEntryRepository
import models.ScheduleEntry
import service.abstracts.{Create, Get}

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class ScheduleService @Inject() (
    val repo: ScheduleEntryRepository,
    implicit val ctx: ExecutionContext
) extends Get[UUID, ScheduleEntry]
    with Create[ScheduleEntry]
