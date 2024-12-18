package service

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import database.repos.ModuleInStudyProgramRepository
import models.Course
import models.Season
import models.Semester

@Singleton
final class CoursePopulationService @Inject() (
    private val seasonService: SeasonService,
    private val moduleInStudyProgramRepository: ModuleInStudyProgramRepository,
    private implicit val ctx: ExecutionContext
) {
  private def isSemester(semester: Semester, season: Season) =
    season.id match {
      case "ws"    => semester.abbrev.toLowerCase.startsWith("wise")
      case "ss"    => semester.abbrev.toLowerCase.startsWith("sose")
      case "ws_ss" => true
    }

  def populate(semester: Semester): Future[Seq[Course]] =
    for {
      seasons <- seasonService
        .all()
        .map(_.collect { case s if isSemester(semester, s) => s.id })
      modules <- moduleInStudyProgramRepository.allModulesInSeason(seasons)
    } yield for {
      module <- modules
      part   <- module.parts
    } yield Course(UUID.randomUUID(), semester.id, module.id, part)
}
