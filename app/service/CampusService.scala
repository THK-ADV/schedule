package service

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext

import database.repos.CampusRepository
import models.Campus
import service.abstracts.Create
import service.abstracts.Get

@Singleton
final class CampusService @Inject() (
    val repo: CampusRepository,
    implicit val ctx: ExecutionContext
) extends Get[UUID, Campus]
    with Create[UUID, Campus]
