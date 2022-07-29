package service

import database.SQLDateConverter
import database.repos.ScheduleRepository
import database.tables.{ScheduleDbEntry, ScheduleTable}
import models.{Schedule, ScheduleJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class ScheduleService @Inject() (val repo: ScheduleRepository)
    extends Service[ScheduleJson, Schedule, ScheduleDbEntry, ScheduleTable]
    with SQLDateConverter {

  override protected def toUniqueDbEntry(json: ScheduleJson, id: Option[UUID]) =
    ScheduleDbEntry(
      json.course,
      json.room,
      json.moduleExaminationRegulation,
      json.date,
      json.start,
      json.end,
      json.status,
      now(),
      id getOrElse UUID.randomUUID
    )

  override protected def validate(json: ScheduleJson) =
    Option.unless(json.start.isBefore(json.end))(
      new Throwable(
        s"start should be before end, but was ${json.start} - ${json.end}"
      )
    )

  def fromIds(ids: List[UUID], atomic: Boolean): Future[Seq[Schedule]] =
    all(Map("id" -> ids.map(_.toString)), atomic)
}
