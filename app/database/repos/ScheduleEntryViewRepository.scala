package database.repos

import controllers.PreferredLanguage
import database.tables.ScheduleEntryViewTable
import models.ScheduleEntryView
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import java.time.LocalDateTime
import javax.inject.{Inject, Singleton}
import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContext, Future}

@Singleton
final class ScheduleEntryViewRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile] {

  import ScheduleEntryView._
  import profile.api._

  protected val tableQuery = TableQuery[ScheduleEntryViewTable]

  def all(
      from: LocalDateTime,
      to: LocalDateTime,
      lang: PreferredLanguage
  ): Future[Iterable[ScheduleEntryView.View]] = {
    import database.tables.localDateTimeColumnType
    db.run(
      tableQuery
        .filter(a => a.start >= from && a.end <= to)
        .result
        .map(_.groupBy(_.id).map { case (id, xs) =>
          val start = ListBuffer.empty[LocalDateTime]
          val end = ListBuffer.empty[LocalDateTime]
          val room = ListBuffer.empty[Room]
          val courseLabel = ListBuffer.empty[String]
          val module = ListBuffer.empty[Module]
          val supervisors = ListBuffer.empty[ModuleSupervisor]
          val studyPrograms = ListBuffer.empty[StudyProgram[String, String]]

          xs.foreach { (x: ScheduleEntryView.DB) =>
            if (!start.contains(x.start)) start += x.start
            if (!end.contains(x.end)) end += x.end
            if (!room.contains(x.room)) room += x.room
            val cl =
              if (lang.value == "de") x.courseLabel._1 else x.courseLabel._2
            if (!courseLabel.contains(cl)) courseLabel += cl
            if (!module.contains(x.module)) module += x.module
            if (!supervisors.contains(x.supervisor)) supervisors += x.supervisor
            val spLabel =
              if (lang.value == "de") x.studyProgram.label._1
              else x.studyProgram.label._2
            val tuLabel =
              if (lang.value == "de") x.studyProgram.teachingUnitLabel._1
              else x.studyProgram.teachingUnitLabel._2
            if (!studyPrograms.exists(_.id == x.studyProgram.id))
              studyPrograms += x.studyProgram
                .copy(label = spLabel, teachingUnitLabel = tuLabel)
          }

          assert(start.size == 1)
          assert(end.size == 1)
          assert(courseLabel.size == 1)
          assert(module.size == 1)

          ScheduleEntryView(
            id,
            start.head,
            end.head,
            room.toList,
            courseLabel.head,
            module.head,
            supervisors.toList,
            studyPrograms.toList
          )
        })
    )
  }
}
