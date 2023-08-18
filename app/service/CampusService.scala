package service

import database.repos.CampusRepository
import models.Campus
import service.abstracts.{Create, Get}

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class CampusService @Inject() (
    val repo: CampusRepository,
    implicit val ctx: ExecutionContext
) extends Get[UUID, Campus]
    with Create[Campus]
