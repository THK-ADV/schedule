package controllers.bootstrap

import models.{Course, CourseId, Module, Room, Semester, StudyProgram}
import ops.DateOps

import java.nio.file.{Files, Path}
import java.time.{LocalDate, LocalTime}
import java.util.UUID
import scala.jdk.CollectionConverters.IteratorHasAsScala

class HOPSMapping(mappingCSV: Path) {

  case class ModuleEntry(
      fachkuerz: String,
      fachname: String,
      modulkuerz: Option[Int],
      modulbez: String,
      modulverant: String,
      id: UUID
  )

  def parseModules: List[ModuleEntry] = {
    def parse(line: String): Option[ModuleEntry] = {
      val data = line.split(";").map(_.replace("\"", "").trim).toList
      Option.when(data.length > 5 && data(5).nonEmpty) {
        ModuleEntry(
          data(0).trim,
          data(1).trim,
          data(2).trim.toIntOption,
          data(3).trim,
          data(4).trim,
          UUID.fromString(data(5).trim)
        )
      }
    }
    Files
      .lines(mappingCSV)
      .iterator()
      .asScala
      .drop(1)
      .map(parse)
      .collect { case Some(a) => a }
      .toList
  }

  val moduleMapping = parseModules

  def findModule(
      number: Int,
      fachkuerz: String,
      modules: Seq[Module]
  ): Option[Module] = {
    val ids: List[(UUID, String)] = moduleMapping.collect {
      case m if m.modulkuerz.contains(number) => (m.id, m.fachkuerz)
    }.distinct
    val matchedModules = modules.filter(m => ids.exists(a => a._1 == m.id))
    matchedModules.size match {
      case 0 =>
        None
      case 1 =>
        Some(matchedModules.head)
      case _ =>
        val module = ids
          .find(_._2 == fachkuerz)
          .flatMap(a => matchedModules.find(_.id == a._1))
        if (module.isEmpty)
          throw new Exception(s"module not found: $number, $fachkuerz")
        module
    }
  }

  def findRoom(identifier: String, rooms: Seq[Room]): Room =
    rooms
      .find(_.identifier.compareToIgnoreCase(identifier) == 0)
      .getOrElse(throw new Throwable(s"unknown room identifier $identifier"))

  def findDate(weekIndex: Int, semester: Semester): LocalDate =
    semester.lectureStart.plusDays(weekIndex - 1)

  def findTime(startStd: Int): (LocalTime, LocalTime) = {
    val baseStd = 7
    val std1 = baseStd + startStd
    val std2 = std1 + 1
    val start = LocalTime.of(std1, 0, 0)
    val end = LocalTime.of(std2, 0, 0)
    (start, end)
  }

  def findStudyProgram(
      studyProgram: String,
      specialization: Option[String],
      studyPrograms: Seq[StudyProgram]
  ): Seq[StudyProgram] = {
    // TODO add support for specialization when mocogi does so
    val pos = (studyProgram, specialization) match {
      case ("AMA_B", Some("AMFK")) => List("ing_gme3", "ing_gme4")
      case ("AMA_B", Some("AMI"))  => List("ing_gme3", "ing_gme4")
      case ("AMA_B", Some("AMK"))  => List("ing_gme3", "ing_gme4")
      case ("AMA_B", Some("AMFM")) => List("ing_gme3", "ing_gme4")
      case ("AMA_B", Some("AMU"))  => List("ing_gme3", "ing_gme4")
      case ("AMA_B", Some("AMKM")) => List("ing_gme3", "ing_gme4")
      case ("AMA_B", None)         => List("ing_gme3", "ing_gme4")
      case ("WIN_B", Some("WIU"))  => List("ing_wiw3", "ing_wiw4")
      case ("WIN_B", Some("WIE"))  => List("ing_wiw3", "ing_wiw4")
      case ("WIN_B", Some("WIM"))  => List("ing_wiw3", "ing_wiw4")
      case ("WIN_B", None)         => List("ing_wiw3", "ing_wiw4")
      case ("EIW_B", Some("AUI"))  => List("ing_een4")
      case ("EIW_B", Some("ELI"))  => List("ing_een4")
      case ("EIW_B", None)         => List("ing_een4")
      case ("AI_B", None)  => List("inf_inf1", "inf_inf2", "inf_inf1_flex")
      case ("MI_B", None)  => List("inf_mi4")
      case ("WI_B", None)  => List("inf_wi4", "inf_wi5")
      case ("ITM_B", None) => List("inf_itm1", "inf_itm2")
      case ("MI_M", None)  => List("inf_mim4")
      case ("TI_B", None)  => Nil
      case ("DS_M", None)  => List("inf_dsi1")
      case ("WEB_M", None) => List("inf_wsc1")
      case ("BDE_M", None) => List("ing_pdpd3", "ing_pdpd4", "ing_pdpd5")
      case ("AIT_M", None) => List("ing_ait1", "ing_ait3")
      case ("WIN_M", Some("WIER")) => List("ing_wiwm1", "ing_wiwm2")
      case ("WIN_M", Some("WIT"))  => List("ing_wiwm1", "ing_wiwm2")
    }
    if (pos.isEmpty)
      throw new Throwable(
        s"unknown studyProgram $studyProgram with specialization $specialization"
      )
    val found = studyPrograms
      .filter(s => pos.contains(s.poId))
      .distinctBy(_.poId) // TODO remove if spec support is added
    if (pos.size != found.size) {
      throw new Throwable(
        s"not all pos are found: $pos != ${found.map(a => a.poId)}"
      )
    }
    found
  }

  def findCourse(
      semester: Semester,
      module: Module,
      part: String,
      courses: Seq[Course]
  ): Option[Course] = {
    val matchedPart = part match {
      case "V"  => CourseId.Lecture
      case "P"  => CourseId.Practical
      case "UE" => CourseId.Exercise
      case "S"  => CourseId.Seminar
      case "T"  => CourseId.Tutorial
      case _    => throw new Throwable(s"unknown part $part")
    }
    courses
      .find(c =>
        c.semester == semester.id && c.module == module.id && c.courseId == matchedPart
      )
  }
}
