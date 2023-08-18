package service

import database.repos.ModuleInStudyProgramRepository
import models.{Course, Semester}

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class CoursePopulationService @Inject() (
    private val courseService: CourseService,
    private val seasonService: SeasonService,
    private val moduleInStudyProgramRepository: ModuleInStudyProgramRepository,
    private implicit val ctx: ExecutionContext
) {
  private val bothSeasons = "ws_ss"

  def populate(semester: Semester) = {
    for {
      seasons <- seasonService
        .all(atomic = false)
        .map(
          _.filter(season =>
            season.deLabel
              .startsWith(
                semester.label.split(" ").head
              ) || season.id == bothSeasons
          )
        )
      modules <- moduleInStudyProgramRepository.allInSeason(seasons)
      courses = for {
        ((m, _), sp) <- modules
        part <- m.parts
      } yield Course(UUID.randomUUID(), semester.id, m.id, sp.id, part)
      res <- courseService.createMany(courses)
    } yield res
  }

  def delete(semester: Semester) = ???
}
