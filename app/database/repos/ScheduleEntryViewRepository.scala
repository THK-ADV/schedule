package database.repos

import database.repos.abstracts.{Create, Get}
import database.tables.ScheduleEntryViewTable
import models.{ModulePart, ScheduleEntryView}
import org.joda.time.{LocalDate, LocalTime}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContext, Future}

@Singleton
final class ScheduleEntryViewRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._
  import ScheduleEntryView._

  protected val tableQuery = TableQuery[ScheduleEntryViewTable]

  def all(): Future[Iterable[ScheduleEntryView.View]] =
    db.run(
      tableQuery.result.map(_.groupBy(_.id).map { case (id, xs) =>
        val date = ListBuffer.empty[LocalDate]
        val start = ListBuffer.empty[LocalTime]
        val end = ListBuffer.empty[LocalTime]
        val room = ListBuffer.empty[Room]
        val coursePart = ListBuffer.empty[ModulePart]
        val module = ListBuffer.empty[Module]
        val supervisors = ListBuffer.empty[ModuleSupervisor]
        val studyPrograms = ListBuffer.empty[StudyProgram]

        xs.foreach { x: ScheduleEntryView.DB =>
          if (!date.contains(x.date)) date += x.date
          if (!start.contains(x.start)) start += x.start
          if (!end.contains(x.end)) end += x.end
          if (!room.contains(x.room)) room += x.room
          if (!coursePart.contains(x.coursePart)) coursePart += x.coursePart
          if (!module.contains(x.module)) module += x.module
          if (!supervisors.contains(x.supervisor)) supervisors += x.supervisor
          if (!studyPrograms.contains(x.studyProgram))
            studyPrograms += x.studyProgram
        }

        assert(date.size == 1)
        assert(start.size == 1)
        assert(end.size == 1)
        assert(room.size == 1)
        assert(coursePart.size == 1)
        assert(module.size == 1)

        ScheduleEntryView(
          id,
          date.head,
          start.head,
          end.head,
          room.head,
          coursePart.head,
          module.head,
          supervisors.toList,
          studyPrograms.toList
        )
      })
    )
}
