package controllers.bootstrap

import models.{Course, Module, ModulePart, Room, Semester, StudyProgram}
import org.joda.time.format.DateTimeFormat
import org.joda.time.{LocalDate, LocalTime}

import java.util.UUID

object HOPSMapping {

  val timeFmt = DateTimeFormat.forPattern("HH:mm:ss")

  def findModule(number: Int, modules: Seq[Module]): Module = {
    val id = number match {
      case 1341 => "dca56fa6-1952-4b47-bf60-3dcb32e86ada"
    }
    modules
      .find(_.id == UUID.fromString(id))
      .getOrElse(throw new Throwable(s"unknown module number $number"))
  }

  def findRoom(identifier: String, rooms: Seq[Room]): Room =
    rooms
      .find(_.identifier == identifier)
      .getOrElse(throw new Throwable(s"unknown room identifier $identifier"))

  def findDate(weekIndex: Int, semester: Semester): LocalDate =
    semester.lectureStart.plusDays(weekIndex - 1)

  def findTime(startStd: Int): (LocalTime, LocalTime) = {
    val baseStd = 7
    val std1 = baseStd + startStd
    val std2 = std1 + 1
    val start = LocalTime.parse(s"$std1:00:00", timeFmt)
    val end = LocalTime.parse(s"$std2:00:00", timeFmt)
    (start, end)
  }

  def findStudyProgram(
      studyProgram: String,
      specialization: Option[String],
      studyPrograms: Seq[StudyProgram]
  ): Seq[StudyProgram] = {
    val pos = (studyProgram, specialization) match {
      case ("AMA_B", Some("AMFK")) => ???
      case ("AMA_B", Some("AMI"))  => ???
      case ("AMA_B", Some("AMK"))  => ???
      case ("AMA_B", Some("AMFM")) => ???
      case ("AMA_B", Some("AMU"))  => ???
      case ("WIN_B", Some("WIU"))  => ???
      case ("WIN_B", Some("WIE"))  => ???
      case ("WIN_B", Some("WIM"))  => ???
      case ("EIW_B", Some("AUI"))  => ???
      case ("EIW_B", Some("ELI"))  => ???
      case ("AI_B", None)          => List("inf_inf1", "inf_inf2", "inf_inf1_flex")
      case ("MI_B", None)          => List("inf_mi4")
      case ("WI_B", None)          => List("inf_wi4", "inf_wi5")
      case ("ITM_B", None)         => List("inf_itm1", "inf_itm2")
      case ("MI_M", None)          => List("inf_mim4")
      case ("TI_B", None)          => Nil
      case ("DS_M", None)          => List("inf_dsi1")
      case ("WEB_M", None)         => List("inf_wsc1")
      case ("BDE_M", None)         => ???
      case ("AIT_M", None)         => ???
      case ("WIN_M", Some("WIER")) => ???
      case ("WIN_M", Some("WIT"))  => ???
    }
    if (pos.isEmpty)
      throw new Throwable(
        s"unknown studyProgram $studyProgram with specialization $specialization"
      )
    else studyPrograms.filter(s => pos.contains(s.id))
  }

  def findCourse(
      semester: Semester,
      module: Module,
      part: String,
      studyProgram: StudyProgram,
      courses: Seq[Course]
  ): Course = {
    val matchedPart = part match {
      case "V"  => ModulePart.Lecture
      case "P"  => ModulePart.Practical
      case "UE" => ModulePart.Exercise
      case "S"  => ModulePart.Seminar
      case "T"  => ModulePart.Tutorial
      case _    => throw new Throwable(s"unknown part $part")
    }
    courses
      .find(c =>
        c.semester == semester.id && c.module == module.id && c.studyProgram == studyProgram.id && c.part == matchedPart
      )
      .getOrElse(
        throw new Throwable(
          s"""unknown course
           |semester: ${semester.abbrev} (${semester.id})
           |module: ${module.label}, $part (${module.id}
           |studyProgram ${studyProgram.deLabel}, ${studyProgram.poNumber}, ${studyProgram.grade} (${studyProgram.id})""".stripMargin
        )
      )
  }
}
