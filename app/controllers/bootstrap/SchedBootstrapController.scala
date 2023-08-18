package controllers.bootstrap

import controllers.CourseController.CourseJson
import models._
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import pretty.PrettyPrinter
import service._

import java.nio.file.Files
import java.util.{UUID, stream}
import javax.inject.{Inject, Singleton}
import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext

@Singleton
final class SchedBootstrapController @Inject() (
    cc: ControllerComponents,
    teachingUnitService: TeachingUnitService,
    semesterService: SemesterService,
    coursePopulationService: CoursePopulationService,
    campusService: CampusService,
    roomService: RoomService,
    moduleService: ModuleService,
    courseService: CourseService,
    studyProgramService: StudyProgramService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc) {

  import HOPSMapping._

  def createTeachingUnits = Action.async { _ =>
    val tus = List(
      TeachingUnit(
        UUID.randomUUID(),
        "f10",
        "Informatik",
        "Computer Science"
      ),
      TeachingUnit(
        UUID.randomUUID(),
        "f10",
        "Ingenieurwesen",
        "Engineering"
      )
    )
    teachingUnitService
      .createMany(tus)
      .map(xs => Ok(Json.obj("created" -> xs.size)))
  }

  def createSemesters() = Action.async { _ =>
    val df = DateTimeFormat.forPattern("dd.MM.yyyy")
    val semesters = List(
      Semester(
        UUID.randomUUID(),
        "Sommersemester 2023",
        "SoSe 23",
        LocalDate.parse("01.03.2023", df),
        LocalDate.parse("31.08.2023", df),
        LocalDate.parse("20.03.2023", df),
        LocalDate.parse("14.07.2023", df)
      ),
      Semester(
        UUID.randomUUID(),
        "Wintersemester 2023 / 24",
        "WiSe 23 / 24",
        LocalDate.parse("01.09.2023", df),
        LocalDate.parse("29.02.2024", df),
        LocalDate.parse("25.09.2023", df),
        LocalDate.parse("09.02.2024", df)
      ),
      Semester(
        UUID.randomUUID(),
        "Sommersemester 2024",
        "SoSe 24",
        LocalDate.parse("01.03.2024", df),
        LocalDate.parse("31.08.2024", df),
        LocalDate.parse("01.04.2024", df),
        LocalDate.parse("26.07.2024", df)
      ),
      Semester(
        UUID.randomUUID(),
        "Wintersemester 2024 / 25",
        "WiSe 24 / 25",
        LocalDate.parse("01.09.2024", df),
        LocalDate.parse("28.02.2025", df),
        LocalDate.parse("23.09.2024", df),
        LocalDate.parse("07.02.2025", df)
      ),
      Semester(
        UUID.randomUUID(),
        "Sommersemester 2025",
        "SoSe 25",
        LocalDate.parse("01.03.2025", df),
        LocalDate.parse("31.08.2025", df),
        LocalDate.parse("24.03.2025", df),
        LocalDate.parse("18.07.2025", df)
      ),
      Semester(
        UUID.randomUUID(),
        "Wintersemester 2025 / 26",
        "WiSe 25 / 26",
        LocalDate.parse("01.09.2025", df),
        LocalDate.parse("28.02.2026", df),
        LocalDate.parse("22.09.2025", df),
        LocalDate.parse("06.02.2026", df)
      ),
      Semester(
        UUID.randomUUID(),
        "Sommersemester 2026",
        "SoSe 26",
        LocalDate.parse("01.03.2026", df),
        LocalDate.parse("31.08.2026", df),
        LocalDate.parse("06.04.2026", df),
        LocalDate.parse("31.07.2026", df)
      ),
      Semester(
        UUID.randomUUID(),
        "Wintersemester 2026 / 27",
        "WiSe 26 / 27",
        LocalDate.parse("01.09.2026", df),
        LocalDate.parse("28.02.2027", df),
        LocalDate.parse("28.09.2026", df),
        LocalDate.parse("12.02.2027", df)
      ),
      Semester(
        UUID.randomUUID(),
        "Sommersemester 2027",
        "SoSe 27",
        LocalDate.parse("01.03.2027", df),
        LocalDate.parse("31.08.2027", df),
        LocalDate.parse("29.03.2027", df),
        LocalDate.parse("23.07.2027", df)
      ),
      Semester(
        UUID.randomUUID(),
        "Wintersemester 2027 / 28",
        "WiSe 27 / 28",
        LocalDate.parse("01.09.2027", df),
        LocalDate.parse("29.02.2028", df),
        LocalDate.parse("27.09.2027", df),
        LocalDate.parse("11.02.2028", df)
      ),
      Semester(
        UUID.randomUUID(),
        "Sommersemester 2028",
        "SoSe 28",
        LocalDate.parse("01.03.2028", df),
        LocalDate.parse("31.08.2028", df),
        LocalDate.parse("03.04.2028", df),
        LocalDate.parse("28.07.2028", df)
      ),
      Semester(
        UUID.randomUUID(),
        "Wintersemester 2028 / 29",
        "WiSe 28 / 29",
        LocalDate.parse("01.09.2028", df),
        LocalDate.parse("28.02.2029", df),
        LocalDate.parse("25.09.2028", df),
        LocalDate.parse("09.02.2029", df)
      ),
      Semester(
        UUID.randomUUID(),
        "Sommersemester 2029",
        "SoSe 29",
        LocalDate.parse("01.03.2029", df),
        LocalDate.parse("31.08.2029", df),
        LocalDate.parse("26.03.2029", df),
        LocalDate.parse("20.07.2029", df)
      ),
      Semester(
        UUID.randomUUID(),
        "Wintersemester 2029 / 30",
        "WiSe 29 / 30",
        LocalDate.parse("01.09.2029", df),
        LocalDate.parse("28.02.2030", df),
        LocalDate.parse("24.09.2029", df),
        LocalDate.parse("08.02.2030", df)
      ),
      Semester(
        UUID.randomUUID(),
        "Sommersemester 2030",
        "SoSe 30",
        LocalDate.parse("01.03.2030", df),
        LocalDate.parse("31.08.2030", df),
        LocalDate.parse("18.03.2030", df),
        LocalDate.parse("12.07.2030", df)
      )
    )

    semesterService
      .createMany(semesters)
      .map(xs => Ok(Json.obj("created" -> xs.size)))
  }

  def createCourses = Action.async { _ =>
    for {
      semesters <- semesterService.all(
        Map("abbrev" -> Seq("WiSe 23 / 24")),
        atomic = false
      ) if semesters.size == 1
      semester = semesters.head
      courses <- coursePopulationService.populate(
        semester
      ) // TODO delete before
    } yield Ok(Json.obj("created" -> courses.size))
  }

  def createCampus = Action.async { _ =>
    val campus = List(
      Campus(UUID.randomUUID(), "Gummersbach", "gm"),
      Campus(UUID.randomUUID(), "Köln Deutz", "kdz"),
      Campus(UUID.randomUUID(), "Köln Südstadt", "ksdt")
    )
    campusService
      .createMany(campus)
      .map(xs => Ok(Json.obj("created" -> xs.size)))
  }

  def toList[A](res: stream.Stream[Option[A]]): List[A] = {
    val list = ListBuffer[A]()
    res.forEach { a =>
      if (a.isDefined)
        list += a.get
    }
    list.toList
  }

  def createRooms = Action(parse.temporaryFile).async { r =>
    def parseRoom(line: String): Option[HOPSRoom] = {
      val data = line.split(";").map(_.replace("\"", ""))
      Some(
        HOPSRoom(
          data(0),
          data(1),
          data(2),
          if (data.length > 3) data(3).toInt else 0
        )
      )
    }
    def toRoom(campus: Seq[Campus])(hopsRoom: HOPSRoom): Room = {
      val (matchingCampus, label) = hopsRoom.label match {
        case "Köln-Südstadt" =>
          (campus.find(_.abbrev == "ksdt").get, hopsRoom.identifier)
        case "Köln-Deutz" =>
          (campus.find(_.abbrev == "kdz").get, hopsRoom.identifier)
        case _ => (campus.find(_.abbrev == "gm").get, hopsRoom.label)
      }
      Room(
        UUID.randomUUID(),
        matchingCampus.id,
        label,
        hopsRoom.identifier,
        hopsRoom.roomType,
        hopsRoom.capacity
      )
    }
    for {
      campus <- campusService.all(atomic = false)
      hopsRooms: List[HOPSRoom] = toList(
        Files
          .lines(r.body.path)
          .skip(1)
          .map(parseRoom)
          .filter(_.isDefined)
      ) if hopsRooms.size == 115
      rooms = hopsRooms.map(toRoom(campus))
      xs <- roomService.createMany(rooms)
    } yield Ok(Json.obj("created" -> xs.size))
  }

  private def textParsingAction = Action(
    parse.byteString.map(
      _.utf8String
    ) // this will fix the encoding issue encoding issue
  )

  def createMissingCourses = Action.async { _ =>
    val ap1Tut =
      List("inf2", "inf1", "inf1_flex", "itm2", "itm1", "mi4", "wi4", "wi5")
        .map(po =>
          CourseJson(
            UUID.fromString("fe279f6f-711f-4434-aa8c-2a7900daa74f"),
            UUID.fromString("dca56fa6-1952-4b47-bf60-3dcb32e86ada"),
            po,
            ModulePart.Tutorial
          )
        )
    ???
  }

  def createScheduleEntries = textParsingAction.async { r =>
    def parseScheduleEntry(line: String): Option[HOPSScheduleEntry] = {
      val data = line.split(";").map(_.replace("\"", ""))
      if (data.length != 9) {
        println(s"line does not have all fields set: $line")
        return None
      }
      Some(
        HOPSScheduleEntry(
          data(0),
          data(1).toInt,
          data(2).toInt,
          data(3),
          data(4),
          data(5),
          data(6).toInt,
          data(7),
          Option.when(data(8).nonEmpty)(data(8))
        )
      )
    }

    for {
      modules <- moduleService.all(atomic = false)
      rooms <- roomService.all(atomic = false)
      courses <- courseService.all(atomic = false)
      studyPrograms <- studyProgramService.all(atomic = false)
      semesters <- semesterService.all(
        Map("abbrev" -> Seq("WiSe 23 / 24")),
        atomic = false
      ) if semesters.size == 1
      semester = semesters.head
      entries = r.body.linesIterator
        .drop(1)
        .map(parseScheduleEntry)
        .filter(_.isDefined)
        .toList
        .map(_.get)
        .distinct
      res = entries
        .filter(_.moduleLabel == "Algorithmen und Programmierung I")
        .groupBy(_.moduleId)
        .map { case (moduleId, entries) =>
          val matchedModule = findModule(moduleId, modules)
          println(matchedModule)
          entries
            .groupBy(a => (a.roomIdentifier, a.weekIndex, a.startStd, a.part))
            .map {
              case ((roomIdentifier, weekIndex, startStd, part), entries) =>
                val matchedRoom = findRoom(roomIdentifier, rooms)
                val date = findDate(weekIndex, semester)
                val (start, end) = findTime(startStd)
                val reservation = Reservation(
                  UUID.randomUUID(),
                  matchedRoom.id,
                  date,
                  start,
                  end,
                  "HOPS",
                  "Stundenplaneintrag"
                )
                val scheduleEntries = entries.flatMap { scheduleEntry =>
                  val matchedStudyPrograms =
                    findStudyProgram(
                      scheduleEntry.studyProgram,
                      scheduleEntry.specialization,
                      studyPrograms
                    )
                  val matchedCourses = matchedStudyPrograms.map { sp =>
                    findCourse(
                      semester,
                      matchedModule,
                      part,
                      sp,
                      courses
                    )
                  }
                  matchedCourses.map { course =>
                    ScheduleEntry(
                      UUID.randomUUID(),
                      course.id,
                      reservation.id
                    )
                  }
                }
                (reservation, scheduleEntries)
            }
        }
        .toList
        .flatten
    } yield {
      println(
        PrettyPrinter.prettyPrint(res.sortBy(a => (a._1.date, a._1.start)))
      )
      NoContent
    }
  }
}
