package service

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext

import database.repos.ScheduleEntryRepository
import models.ScheduleEntry
import service.abstracts.Get

@Singleton
final class ScheduleService @Inject() (
    val repo: ScheduleEntryRepository,
    implicit val ctx: ExecutionContext
) extends Get[UUID, ScheduleEntry]
