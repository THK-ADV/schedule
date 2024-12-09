package service

import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import database.repos.SemesterPlanEntryRepository
import models.SemesterPlanEntry

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
