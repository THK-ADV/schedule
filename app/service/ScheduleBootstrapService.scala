package service

import database.SQLDateConverter
import database.tables.{CourseDbEntry, ScheduleWIPDbEntry}
import models.{Course, Schedule, Semester, Weekday}

import java.sql.Timestamp
import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ScheduleBootstrapService @Inject() (
    val courseService: CourseService,
    val scheduleService: ScheduleService,
    val scheduleWIPService: ScheduleWIPService,
    implicit val ctx: ExecutionContext
) extends SQLDateConverter {

  def now() = new Timestamp(System.currentTimeMillis())

  def bootstrap(src: Semester, dest: Semester) = {
    val semesterFilter = Map("semester" -> Seq(src.id.toString))

    for {
      existing <- scheduleWIPService.count(semesterFilter) if existing == 0
      newCourses <- bootstrapCourses(semesterFilter, dest)
      newWIPScheduleEntries <- bootstrapScheduleEntries(
        semesterFilter,
        newCourses
      )
    } yield (newCourses.map(_._2), newWIPScheduleEntries)
  }

  private def bootstrapScheduleEntries(
      semesterFilter: Map[String, Seq[String]],
      newCourses: Seq[(Course, Course)]
  ) = {
    def create(c: Schedule): ScheduleWIPDbEntry =
      ScheduleWIPDbEntry(
        newCourses.find(_._1.id == c.courseId).get._2.id,
        c.roomId,
        c.moduleExaminationRegulationId,
        Weekday(c.date),
        c.start,
        c.end,
        "",
        "",
        0,
        now(),
        UUID.randomUUID()
      )

    def extractScheme(schedule: Seq[Schedule]): Seq[Schedule] = {
      val sorted = schedule.sortBy(_.date)

    }

    for {
      previous <- scheduleService.all(semesterFilter, atomic = false)
      newScheduleEntries = extractScheme(previous).map(create)
      _ <- scheduleWIPService.repo.forceInsert(newScheduleEntries)
    } yield newScheduleEntries
  }

  private def bootstrapCourses(
      semesterFilter: Map[String, Seq[String]],
      dest: Semester
  ) = {
    def copy(c: Course): CourseDbEntry = {
      CourseDbEntry(
        c.lecturerId,
        dest.id,
        c.subModuleId,
        c.interval,
        c.courseType,
        now(),
        UUID.randomUUID
      )
    }

    for {
      previous <- courseService.all(semesterFilter, atomic = false)
      // TODO add support for new courses which should be derived from new subModules
      res <- Future.sequence(
        previous.map { p =>
          val e = copy(p)
          courseService.repo
            .create(
              e,
              courseService.checkUniqueCols(
                e.semester,
                e.lecturer,
                e.subModule,
                e.courseType
              )
            )
            .map(c => (p, c))
        }
      )
    } yield res
  }
}
