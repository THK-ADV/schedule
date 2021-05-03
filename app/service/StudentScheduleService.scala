package service

import database.repos.StudentScheduleRepository
import database.tables.StudentScheduleTable
import models.{StudentSchedule, StudentScheduleJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class StudentScheduleService @Inject() (val repo: StudentScheduleRepository)
    extends Service[
      StudentScheduleJson,
      StudentSchedule,
      StudentScheduleTable
    ] {

  override protected def toModel(json: StudentScheduleJson, id: Option[UUID]) =
    StudentSchedule(json.student, json.schedule, id getOrElse UUID.randomUUID)

  override protected def canUpdate(
      json: StudentScheduleJson,
      existing: StudentSchedule
  ): Boolean =
    existing.student == json.student && existing.schedule == json.schedule

  override protected def uniqueCols(
      json: StudentScheduleJson,
      table: StudentScheduleTable
  ) =
    List(table.hasSchedule(json.schedule), table.hasUser(json.student))

  override protected def validate(json: StudentScheduleJson) = None
}
