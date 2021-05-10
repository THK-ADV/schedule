package service

import database.repos.StudentScheduleRepository
import database.tables.{StudentScheduleDbEntry, StudentScheduleTable}
import models.{StudentSchedule, StudentScheduleJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class StudentScheduleService @Inject() (val repo: StudentScheduleRepository)
    extends Service[
      StudentScheduleJson,
      StudentSchedule,
      StudentScheduleDbEntry,
      StudentScheduleTable
    ] {

  override protected def toUniqueDbEntry(
      json: StudentScheduleJson,
      id: Option[UUID]
  ) =
    StudentScheduleDbEntry(
      json.student,
      json.schedule,
      now(),
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: StudentScheduleJson,
      existing: StudentScheduleDbEntry
  ): Boolean =
    existing.student == json.student && existing.schedule == json.schedule

  override protected def uniqueCols(
      json: StudentScheduleJson,
      table: StudentScheduleTable
  ) =
    List(table.hasSchedule(json.schedule), table.hasUser(json.student))

  override protected def validate(json: StudentScheduleJson) = None
}
