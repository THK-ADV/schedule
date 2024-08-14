package service

import database.repos.TeachingUnitRepository
import models.TeachingUnit
import service.abstracts.{Create, Get}

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class TeachingUnitService @Inject() (
    val repo: TeachingUnitRepository,
    implicit val ctx: ExecutionContext
) extends Get[UUID, TeachingUnit]
    with Create[UUID, TeachingUnit]
