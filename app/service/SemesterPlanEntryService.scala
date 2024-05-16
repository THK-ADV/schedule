package service

import database.repos.SemesterPlanEntryRepository
import models.SemesterPlanEntry
import service.abstracts.{Create, Get}

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class SemesterPlanEntryService @Inject() (
    val repo: SemesterPlanEntryRepository,
    implicit val ctx: ExecutionContext
) extends Get[UUID, SemesterPlanEntry]
    with Create[SemesterPlanEntry]
