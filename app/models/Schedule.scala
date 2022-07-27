package models

import database.SQLDateConverter
import database.tables.ScheduleDbEntry
import models.Course.CourseAtom
import models.ModuleExaminationRegulation.ModuleExaminationRegulationAtom
import org.joda.time.{LocalDate, LocalTime}

import java.util.UUID

sealed trait Schedule extends UniqueEntity {
  def courseId: UUID

  def roomId: UUID

  def moduleExaminationRegulationId: UUID

  def date: LocalDate

  def start: LocalTime

  def end: LocalTime

  def status: ScheduleEntryStatus
}

object Schedule extends SQLDateConverter {

  case class ScheduleDefault(
      course: UUID,
      room: UUID,
      moduleExaminationRegulation: UUID,
      date: LocalDate,
      start: LocalTime,
      end: LocalTime,
      status: ScheduleEntryStatus,
      id: UUID
  ) extends Schedule {
    override def courseId = course

    override def roomId = room

    override def moduleExaminationRegulationId = moduleExaminationRegulation
  }

  case class ScheduleAtom(
      course: CourseAtom,
      room: Room,
      moduleExaminationRegulation: ModuleExaminationRegulationAtom,
      date: LocalDate,
      start: LocalTime,
      end: LocalTime,
      status: ScheduleEntryStatus,
      id: UUID
  ) extends Schedule {
    override def courseId = course.id

    override def roomId = room.id

    override def moduleExaminationRegulationId = moduleExaminationRegulation.id
  }

  def apply(db: ScheduleDbEntry): ScheduleDefault =
    ScheduleDefault(
      db.course,
      db.room,
      db.moduleExaminationRegulation,
      db.date,
      db.start,
      db.end,
      db.status,
      db.id
    )
}
