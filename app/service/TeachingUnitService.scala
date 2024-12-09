package service

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext

import database.repos.TeachingUnitRepository
import models.TeachingUnit
import service.abstracts.Create
import service.abstracts.Get

@Singleton
final class TeachingUnitService @Inject() (
    val repo: TeachingUnitRepository,
    implicit val ctx: ExecutionContext
) extends Get[UUID, TeachingUnit]
    with Create[UUID, TeachingUnit]
