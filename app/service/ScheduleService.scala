package service

import database.repos.ScheduleRepository
import database.tables.ScheduleTable
import models.{Schedule, ScheduleJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class ScheduleService @Inject() (val repo: ScheduleRepository)
    extends Service[ScheduleJson, Schedule, ScheduleTable] {

  override protected def toModel(json: ScheduleJson, id: Option[UUID]) =
    Schedule(
      json.course,
      json.room,
      json.date,
      json.start,
      json.end,
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: ScheduleJson,
      existing: Schedule
  ): Boolean =
    existing.course == json.course &&
      existing.room == json.room &&
      existing.date == json.date &&
      existing.start == json.start &&
      existing.end == json.end

  override protected def uniqueCols(json: ScheduleJson, table: ScheduleTable) =
    List(
      table.hasCourse(json.course),
      table.hasRoom(json.room),
      table.onDate(json.date),
      table.onStart(json.start),
      table.onEnd(json.end)
    )

  override protected def validate(json: ScheduleJson) =
    Option.unless(json.start.isBefore(json.end))(
      new Throwable(
        s"start should be before end, but was ${json.start} - ${json.end}"
      )
    )
}
