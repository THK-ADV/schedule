package models

import org.joda.time.{LocalDate, LocalTime}
import play.api.libs.json.{Json, Writes}

import java.util.UUID

case class ScheduleEntryView[Supervisor, StudyProgram](
    id: UUID,
    date: LocalDate,
    start: LocalTime,
    end: LocalTime,
    room: ScheduleEntryView.Room,
    coursePart: ModulePart,
    module: ScheduleEntryView.Module,
    supervisor: Supervisor,
    studyProgram: StudyProgram
) extends UniqueEntity[UUID]

object ScheduleEntryView {
  type DB = ScheduleEntryView[ModuleSupervisor, StudyProgram]
  type View = ScheduleEntryView[List[ModuleSupervisor], List[StudyProgram]]

  implicit def supervisorWrites: Writes[ModuleSupervisor] = Json.writes

  implicit def moduleWrites: Writes[Module] = Json.writes

  implicit def roomWrites: Writes[Room] = Json.writes

  implicit def studyProgramWrites: Writes[StudyProgram] = Json.writes

  implicit def writes: Writes[View] = view =>
    Json.obj(
      "id" -> view.id,
      "date" -> view.date,
      "start" -> view.start,
      "end" -> view.end,
      "room" -> Json.toJson(view.room),
      "coursePart" -> Json.toJson(view.coursePart),
      "module" -> Json.toJson(view.module),
      "supervisor" -> Json.toJson(view.supervisor),
      "studyProgram" -> Json.toJson(view.studyProgram)
    )

  case class ModuleSupervisor(
      id: String,
      kind: String,
      firstname: String,
      lastname: String,
      title: String
  )

  case class Module(
      id: UUID,
      label: String,
      abbrev: String,
      language: String
  )

  case class Room(
      id: UUID,
      identifier: String,
      campusId: UUID,
      campusLabel: String
  )

  case class StudyProgram(
      id: UUID,
      deLabel: String,
      enLabel: String,
      poId: String,
      poNumber: Int,
      degreeId: String,
      degreeLabel: String,
      teachingUnitId: UUID,
      teachingUnitDeLabel: String,
      teachingUnitEnLabel: String,
      mandatory: Boolean,
      isFocus: Boolean
  )
}
