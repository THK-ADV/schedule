package service

import database.repos.SemesterPlanEntryRepository
import models.SemesterPlanEntry

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
final class SemesterPlanEntryService @Inject() (
    val repo: SemesterPlanEntryRepository,
    implicit val ctx: ExecutionContext
) {
  def createManyForce(
      elems: List[SemesterPlanEntry]
  ): Future[Seq[SemesterPlanEntry]] =
    repo.createMany(elems)
}
