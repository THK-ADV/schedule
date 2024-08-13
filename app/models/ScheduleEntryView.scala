package models

import play.api.libs.json.{Json, Writes}

import java.time.LocalDateTime
import java.util.UUID

case class ScheduleEntryView[Supervisor, StudyProgram, Room, CourseLabel](
    id: UUID,
    start: LocalDateTime,
    end: LocalDateTime,
    room: Room,
    courseLabel: CourseLabel,
    module: ScheduleEntryView.Module,
    supervisor: Supervisor,
    studyProgram: StudyProgram
) extends UniqueEntity[UUID]

object ScheduleEntryView {
  type DB = ScheduleEntryView[
    ModuleSupervisor,
    StudyProgram[(String, String), (String, String)],
    Room,
    (String, String)
  ]
  type View = ScheduleEntryView[
    List[ModuleSupervisor],
    List[StudyProgram[String, String]],
    List[Room],
    String
  ]

  implicit def supervisorWrites: Writes[ModuleSupervisor] = Json.writes

  implicit def moduleWrites: Writes[Module] = Json.writes

  implicit def roomWrites: Writes[Room] = Json.writes

  implicit def studyProgramWrites: Writes[StudyProgram[String, String]] =
    Json.writes

  implicit def writes: Writes[View] = view =>
    Json.obj(
      "id" -> view.id,
      "start" -> view.start,
      "end" -> view.end,
      "rooms" -> Json.toJson(view.room),
      "courseLabel" -> view.courseLabel,
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

  case class StudyProgram[Label, TeachingUnitLabel](
      id: UUID,
      label: Label,
      poId: String,
      poNumber: Int,
      degreeId: String,
      degreeLabel: String,
      teachingUnitId: UUID,
      teachingUnitLabel: TeachingUnitLabel,
      mandatory: Boolean,
      isFocus: Boolean,
      recommendedSemester: List[Int]
  )
}
