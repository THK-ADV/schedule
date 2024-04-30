package database.tables

import database.UUIDUniqueColumn
import models.ScheduleEntryView
import org.joda.time.{LocalDate, LocalTime}
import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape

import java.util.UUID

final class ScheduleEntryViewTable(tag: Tag)
    extends Table[ScheduleEntryView.DB](tag, "schedule_entry_view")
    with UUIDUniqueColumn {

  import ScheduleEntryView._
  import database.tables.{localDateColumnType, localTimeColumnType}

  override def id = column[UUID]("s_id", O.PrimaryKey)

  def date = column[LocalDate]("s_date")

  def start = column[LocalTime]("s_start")

  def end = column[LocalTime]("s_end")

  def roomId = column[UUID]("room_id")

  def roomIdentifier = column[String]("room_identifier")

  def campusId = column[UUID]("campus_id")

  def campusLabel = column[String]("campus_label")

  def courseDeLabel = column[String]("course_de_label")

  def courseEnLabel = column[String]("course_en_label")

  def moduleId = column[UUID]("module_id")

  def moduleLabel = column[String]("module_label")

  def moduleAbbrev = column[String]("module_abbrev")

  def moduleLanguage = column[String]("module_language")

  def moduleLecturerId = column[String]("module_lecturer_id")

  def moduleLecturerKind = column[String]("module_lecturer_kind")

  def moduleLecturerFirstname = column[String]("module_lecturer_firstname")

  def moduleLecturerLastname = column[String]("module_lecturer_lastname")

  def moduleLecturerTitle = column[String]("module_lecturer_title")

  def spId = column[UUID]("sp_id")

  def spDeLabel = column[String]("sp_de_label")

  def spEnLabel = column[String]("sp_en_label")

  def poId = column[String]("po_id")

  def poNumber = column[Int]("po_number")

  def degreeId = column[String]("degree_id")

  def degreeLabel = column[String]("degree_label")

  def teachingUnitId = column[UUID]("teaching_unit_id")

  def teachingUnitDeLabel = column[String]("teaching_unit_de_label")

  def teachingUnitEnLabel = column[String]("teaching_unit_en_label")

  def mandatory = column[Boolean]("mandatory")

  def focus = column[Boolean]("focus")

  def recommendedSemester = column[String]("recommended_semester")

  override def * : ProvenShape[ScheduleEntryView.DB] = (
    id,
    date,
    start,
    end,
    (roomId, roomIdentifier, campusId, campusLabel),
    (courseDeLabel, courseEnLabel),
    (moduleId, moduleLabel, moduleAbbrev, moduleLanguage),
    (
      moduleLecturerId,
      moduleLecturerKind,
      moduleLecturerFirstname,
      moduleLecturerLastname,
      moduleLecturerTitle
    ),
    spId,
    (spDeLabel, spEnLabel),
    poId,
    poNumber,
    degreeId,
    degreeLabel,
    teachingUnitId,
    (teachingUnitDeLabel, teachingUnitEnLabel),
    mandatory,
    focus,
    recommendedSemester
  ) <> (mapRow, unmapRow)

  private def mapRow: (
      (
          UUID,
          LocalDate,
          LocalTime,
          LocalTime,
          (UUID, String, UUID, String),
          (String, String),
          (UUID, String, String, String),
          (String, String, String, String, String),
          UUID,
          (String, String),
          String,
          Int,
          String,
          String,
          UUID,
          (String, String),
          Boolean,
          Boolean,
          String
      )
  ) => ScheduleEntryView.DB = {
    case (
          id,
          date,
          start,
          end,
          (roomId, roomIdentifier, campusId, campusLabel),
          courseLabel,
          (moduleId, moduleLabel, moduleAbbrev, moduleLanguage),
          (
            moduleLecturerId,
            moduleLecturerKind,
            moduleLecturerFirstname,
            moduleLecturerLastname,
            moduleLecturerTitle
          ),
          spId,
          spLabel,
          poId,
          poNumber,
          degreeId,
          degreeLabel,
          teachingUnitId,
          teachingUnitLabel,
          mandatory,
          focus,
          recommendedSemester
        ) =>
      ScheduleEntryView(
        id,
        date,
        start,
        end,
        Room(roomId, roomIdentifier, campusId, campusLabel),
        courseLabel,
        Module(moduleId, moduleLabel, moduleAbbrev, moduleLanguage),
        ModuleSupervisor(
          moduleLecturerId,
          moduleLecturerKind,
          moduleLecturerFirstname,
          moduleLecturerLastname,
          moduleLecturerTitle
        ),
        StudyProgram(
          spId,
          spLabel,
          poId,
          poNumber,
          degreeId,
          degreeLabel,
          teachingUnitId,
          teachingUnitLabel,
          mandatory,
          focus,
          toRecommendedSemester(recommendedSemester)
        )
      )
  }

  private def unmapRow: ScheduleEntryView.DB => Option[
    (
        UUID,
        LocalDate,
        LocalTime,
        LocalTime,
        (UUID, String, UUID, String),
        (String, String),
        (UUID, String, String, String),
        (String, String, String, String, String),
        UUID,
        (String, String),
        String,
        Int,
        String,
        String,
        UUID,
        (String, String),
        Boolean,
        Boolean,
        String
    )
  ] = { a =>
    Option(
      (
        a.id,
        a.date,
        a.start,
        a.end,
        (
          a.room.id,
          a.room.identifier,
          a.room.campusId,
          a.room.campusLabel
        ),
        a.courseLabel,
        (a.module.id, a.module.label, a.module.abbrev, a.module.language),
        (
          a.supervisor.id,
          a.supervisor.kind,
          a.supervisor.firstname,
          a.supervisor.lastname,
          a.supervisor.title
        ),
        a.studyProgram.id,
        a.studyProgram.label,
        a.studyProgram.poId,
        a.studyProgram.poNumber,
        a.studyProgram.degreeId,
        a.studyProgram.degreeLabel,
        a.studyProgram.teachingUnitId,
        a.studyProgram.teachingUnitLabel,
        a.studyProgram.mandatory,
        a.studyProgram.isFocus,
        fromRecommendedSemester(a.studyProgram.recommendedSemester)
      )
    )
  }

  private def fromRecommendedSemester(xs: List[Int]): String =
    if (xs.isEmpty) "" else xs.mkString(",")

  private def toRecommendedSemester(s: String): List[Int] =
    if (s.isEmpty) Nil
    else s.split(",").map(_.toInt).toList
}
