package service

import database.SQLDateConverter
import database.repos.ScheduleWIPRepository
import database.tables.{ScheduleWIPDbEntry, ScheduleWIPTable}
import models.{ScheduleWIP, ScheduleWIPJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class ScheduleWIPService @Inject() (val repo: ScheduleWIPRepository)
    extends Service[
      ScheduleWIPJson,
      ScheduleWIP,
      ScheduleWIPDbEntry,
      ScheduleWIPTable
    ]
    with SQLDateConverter {

  override protected def toUniqueDbEntry(
      json: ScheduleWIPJson,
      id: Option[UUID]
  ) =
    ScheduleWIPDbEntry(
      json.course,
      json.room,
      json.moduleExaminationRegulation,
      json.date,
      json.start,
      json.end,
      json.notes,
      json.history,
      json.priority,
      now(),
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: ScheduleWIPJson,
      existing: ScheduleWIPDbEntry
  ): Boolean = true

  override protected def uniqueCols(json: ScheduleWIPJson) =
    Nil

  override protected def validate(json: ScheduleWIPJson) =
    Option.unless(json.start.isBefore(json.end))(
      new Throwable(
        s"start should be before end, but was ${json.start} - ${json.end}"
      )
    )
}
