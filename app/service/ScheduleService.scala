package service

import database.SQLDateConverter
import database.repos.ScheduleRepository
import database.tables.{ScheduleDbEntry, ScheduleTable}
import models.{Schedule, ScheduleJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class ScheduleService @Inject() (val repo: ScheduleRepository)
    extends Service[ScheduleJson, Schedule, ScheduleDbEntry, ScheduleTable]
    with SQLDateConverter {

  override protected def toUniqueDbEntry(json: ScheduleJson, id: Option[UUID]) =
    ScheduleDbEntry(
      json.course,
      json.room,
      json.date,
      json.start,
      json.end,
      now(),
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: ScheduleJson,
      existing: ScheduleDbEntry
  ): Boolean =
    existing.course == json.course &&
      existing.room == json.room &&
      toLocalDate(existing.date) == json.date &&
      toLocalTime(existing.start) == json.start &&
      toLocalTime(existing.end) == json.end

  override protected def uniqueCols(json: ScheduleJson) =
    List(
      _.hasCourse(json.course),
      _.hasRoom(json.room),
      _.onDate(json.date),
      _.onStart(json.start),
      _.onEnd(json.end)
    )

  override protected def validate(json: ScheduleJson) =
    Option.unless(json.start.isBefore(json.end))(
      new Throwable(
        s"start should be before end, but was ${json.start} - ${json.end}"
      )
    )
}
