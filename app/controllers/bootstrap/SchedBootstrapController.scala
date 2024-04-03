package controllers.bootstrap

import database.repos.ScheduleEntryRepository
import database.tables.ModuleStudyProgramScheduleEntry
import models._
import org.joda.time.format.DateTimeFormat
import org.joda.time.{LocalDate, LocalTime, Weeks}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import service._

import java.nio.file.{Files, Paths}
import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext
import scala.jdk.CollectionConverters.IteratorHasAsScala

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
    moduleInStudyProgramService: ModuleInStudyProgramService,
    scheduleEntryRepository: ScheduleEntryRepository,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc) {

  case class ScheduleEntryProtocol(
      moduleInStudyProgram: UUID,
      course: UUID,
      room: UUID,
      date: LocalDate,
      start: LocalTime,
      end: LocalTime
  )

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

  def createCampus = Action.async { _ =>
    val campus = List(
      Campus(UUID.randomUUID(), "Gummersbach", "gm"),
      Campus(UUID.randomUUID(), "Köln Deutz", "kdz"),
      Campus(UUID.randomUUID(), "Köln Südstadt", "ksdt"),
      Campus(UUID.randomUUID(), "Sonstige", "other")
    )
    campusService
      .createMany(campus)
      .map(xs => Ok(Json.obj("created" -> xs.size)))
  }

  def createRooms = Action(parse.temporaryFile).async { r =>
    def parseRoom(line: String): HOPSRoom = {
      val data = line.split(";").map(_.replace("\"", ""))
      HOPSRoom(
        data(0).trim,
        data(1).trim,
        data(2).trim,
        if (data.length > 3) data(3).toInt else 0
      )
    }
    def toRoom(campus: Seq[Campus])(hopsRoom: HOPSRoom): Room = {
      val (matchingCampus, label) = {
        if (hopsRoom.label == "Zoom")
          (campus.find(_.abbrev == "other").get, hopsRoom.label)
        else
          hopsRoom.label match {
            case "Köln-Südstadt" =>
              (campus.find(_.abbrev == "ksdt").get, hopsRoom.identifier)
            case "Köln-Deutz" =>
              (campus.find(_.abbrev == "kdz").get, hopsRoom.identifier)
            case _ => (campus.find(_.abbrev == "gm").get, hopsRoom.label)
          }
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
      campus <- campusService.all()
      rooms = Files
        .lines(r.body.path)
        .skip(1)
        .iterator()
        .asScala
        .map(parseRoom _ andThen toRoom(campus))
        .toList
      xs <- roomService.createMany(rooms)
    } yield Ok(Json.obj("created" -> xs.size))
  }

  def createCourses = Action.async { _ =>
    val semester = semesterService
      .allWithFilter(Map("abbrev" -> Seq("WiSe 23 / 24")))
      .map(_.head)
    for {
      semester <- semester
      _ <- courseService.deleteAll()
      courses <- coursePopulationService.populate(semester)
      explicitCourses = List(
        // http://lwivs49.gm.fh-koeln.de:8081/modules/b126ec6a-0241-4f6b-90d6-c0ecad8e3dd4
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("b126ec6a-0241-4f6b-90d6-c0ecad8e3dd4"),
          ModulePart.Lecture
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/d4ad3104-c495-4acb-8ce0-a73881210650
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("d4ad3104-c495-4acb-8ce0-a73881210650"),
          ModulePart.Lecture
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/6716c404-632a-4918-85a1-0ba0051bacb5
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("6716c404-632a-4918-85a1-0ba0051bacb5"),
          ModulePart.Lecture
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/42123d0e-f9ee-4117-99f4-0c985aca1313
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("42123d0e-f9ee-4117-99f4-0c985aca1313"),
          ModulePart.Lecture
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/178d394f-0b03-4566-80b6-272fbd49f760
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("178d394f-0b03-4566-80b6-272fbd49f760"),
          ModulePart.Seminar
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/5941afae-a356-4dce-9e9b-1b70371c8202
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("5941afae-a356-4dce-9e9b-1b70371c8202"),
          ModulePart.Lecture
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/e045b42f-f56b-4f2f-94e3-fa56f3660b89
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("e045b42f-f56b-4f2f-94e3-fa56f3660b89"),
          ModulePart.Lecture
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/d4ad3104-c495-4acb-8ce0-a73881210650
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("d4ad3104-c495-4acb-8ce0-a73881210650"),
          ModulePart.Tutorial
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/5941afae-a356-4dce-9e9b-1b70371c8202
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("5941afae-a356-4dce-9e9b-1b70371c8202"),
          ModulePart.Tutorial
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/315b421f-50fc-44a0-b584-8b38c9f53c87
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("315b421f-50fc-44a0-b584-8b38c9f53c87"),
          ModulePart.Lecture
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/03fa8ca0-4979-4514-a0aa-b0d2e73e07f8
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("03fa8ca0-4979-4514-a0aa-b0d2e73e07f8"),
          ModulePart.Lecture
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/dfabe6dd-6f9a-498d-912e-3bb84fe68bee
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("dfabe6dd-6f9a-498d-912e-3bb84fe68bee"),
          ModulePart.Lecture
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/9ae4f461-817e-479b-90e1-071ec82c2079
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("9ae4f461-817e-479b-90e1-071ec82c2079"),
          ModulePart.Lecture
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/9f464cde-5237-4832-a7ec-23afc46cf4eb
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("9f464cde-5237-4832-a7ec-23afc46cf4eb"),
          ModulePart.Seminar
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/3a4077fa-fb35-4e83-a54f-1473522c57c9
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("3a4077fa-fb35-4e83-a54f-1473522c57c9"),
          ModulePart.Lecture
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/5b712502-14b2-49f1-9d77-a2ed852917c1
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("5b712502-14b2-49f1-9d77-a2ed852917c1"),
          ModulePart.Lecture
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/5232fe71-7bd2-4528-a0ff-9cca594120a5
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("5232fe71-7bd2-4528-a0ff-9cca594120a5"),
          ModulePart.Lecture
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/5d3d819a-22ba-4bf6-a2e2-e9feb7e3e71e
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("5d3d819a-22ba-4bf6-a2e2-e9feb7e3e71e"),
          ModulePart.Lecture
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/2c0d9243-3f16-43be-bec8-e85ec6e5153e
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("2c0d9243-3f16-43be-bec8-e85ec6e5153e"),
          ModulePart.Lecture
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/b694276e-de90-40e8-b2cd-ce1d22407fd3
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("b694276e-de90-40e8-b2cd-ce1d22407fd3"),
          ModulePart.Lecture
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/71105c23-d165-472c-8bcd-cab66c9a08f7
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("71105c23-d165-472c-8bcd-cab66c9a08f7"),
          ModulePart.Lecture
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/ca272b86-8a95-4e8d-b56f-4c63d389b51f
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("ca272b86-8a95-4e8d-b56f-4c63d389b51f"),
          ModulePart.Lecture
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/dca56fa6-1952-4b47-bf60-3dcb32e86ada
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("dca56fa6-1952-4b47-bf60-3dcb32e86ada"),
          ModulePart.Tutorial
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/41ca9f81-63f1-4dba-9c33-adae878ee2ca
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("41ca9f81-63f1-4dba-9c33-adae878ee2ca"),
          ModulePart.Exercise
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/3a5c00ce-c2a9-4d28-aa40-4968b7540448
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("3a5c00ce-c2a9-4d28-aa40-4968b7540448"),
          ModulePart.Lecture
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/069ab61c-b54a-48b5-815d-117fc9de829c
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("069ab61c-b54a-48b5-815d-117fc9de829c"),
          ModulePart.Lecture
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/66cdf64a-a164-46b1-a654-0c95564b563c
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("66cdf64a-a164-46b1-a654-0c95564b563c"),
          ModulePart.Lecture
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/c285df00-5cc3-4e89-8c9e-e5915b581e5d
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("c285df00-5cc3-4e89-8c9e-e5915b581e5d"),
          ModulePart.Lecture
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/15769d73-a0b6-49a5-9b47-3e155a063e20
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("15769d73-a0b6-49a5-9b47-3e155a063e20"),
          ModulePart.Lecture
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/fb968b0d-462c-4354-9322-02617ca801df
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("fb968b0d-462c-4354-9322-02617ca801df"),
          ModulePart.Lecture
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/f88e64e6-8344-4b7f-ad9a-8499fed4196f
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("f88e64e6-8344-4b7f-ad9a-8499fed4196f"),
          ModulePart.Lecture
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/65a319e8-788c-4469-ade1-a10566d87a4a
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("65a319e8-788c-4469-ade1-a10566d87a4a"),
          ModulePart.Lecture
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/876c5d42-f579-4a7c-953c-76895b731ff4
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("876c5d42-f579-4a7c-953c-76895b731ff4"),
          ModulePart.Lecture
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/072df25e-392e-49e7-98c1-400e3fc9f315
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("072df25e-392e-49e7-98c1-400e3fc9f315"),
          ModulePart.Lecture
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/70d904c0-93d8-4b54-a1a1-1f8ed02417ef
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("70d904c0-93d8-4b54-a1a1-1f8ed02417ef"),
          ModulePart.Lecture
        ),
        // http://lwivs49.gm.fh-koeln.de:8081/modules/1cf030cd-f0e5-4fe6-9430-0a4b4fbb308c
        Course(
          UUID.randomUUID(),
          semester.id,
          UUID.fromString("1cf030cd-f0e5-4fe6-9430-0a4b4fbb308c"),
          ModulePart.Lecture
        )
      )
      _ <- courseService.createMany(courses.appendedAll(explicitCourses))
    } yield Ok(Json.obj("created" -> courses.size))
  }

  // this will fix the encoding issue encoding issue
  private def textParsingAction =
    Action(parse.byteString.map(_.utf8String))

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

    val mapping = new HOPSMapping(Paths.get("bootstrap/Mapping.csv"))

    for {
      _ <- scheduleEntryRepository.deleteAll()
      modules <- moduleService.all()
      rooms <- roomService.all()
      courses <- courseService.all()
      studyPrograms <- studyProgramService.all()
      moduleInStudyPrograms <- moduleInStudyProgramService.all()
      semesters <- semesterService.all()
      wise = semesters.find(_.abbrev == "WiSe 23 / 24").get
      sose = semesters.find(_.abbrev == "SoSe 24").get
      entries = r.body.linesIterator
        .drop(1)
        .map(parseScheduleEntry)
        .collect { case Some(a) => a }
        .toList
        .distinct
      (_, scheduleEntryProtocols) = entries
        .filterNot(_.studyProgram == "TI_B")
        .partitionMap { e =>
          mapping.findModule(e.moduleId, e.course, modules) match {
            case Some(module) =>
              val matchedRoom = mapping.findRoom(e.roomIdentifier, rooms)
              val date = mapping.findDate(e.weekIndex, sose)
              val (start, end) = mapping.findTime(e.startStd)
              val matchedStudyPrograms = mapping.findStudyProgram(
                e.studyProgram,
                e.specialization,
                studyPrograms
              )
              mapping.findCourse(wise, module, e.part, courses) match {
                case Some(course) =>
                  val sps = moduleInStudyPrograms
                    .filter(a =>
                      matchedStudyPrograms.exists(
                        _.id == a.studyProgram
                      ) && a.module == module.id
                    )
                  if (sps.isEmpty) {
                    printErr(
                      s"studyProgram associations not found ${(e.course, e.moduleId, e.part)}"
                    )
                    Left(e)
                  } else {
                    Right(
                      sps.map(a =>
                        ScheduleEntryProtocol(
                          a.id,
                          course.id,
                          matchedRoom.id,
                          date,
                          start,
                          end
                        )
                      )
                    )
                  }

                case None =>
                  printErr(
                    s"course not found ${(e.course, e.moduleId, e.part)}"
                  )
                  Left(e)
              }
            case None =>
              printErr(s"module not found ${(e.course, e.moduleId, e.part)}")
              Left(e)
          }
        }
      scheduleEntriesWithStudyProgram = extrapolate(
        squash(
          scheduleEntryProtocols.flatten.distinctBy { a =>
            (a.course, a.moduleInStudyProgram, a.room, a.date, a.start, a.end)
          }
        ),
        sose
      )
      scheduleEntries = scheduleEntriesWithStudyProgram.map(_._1)
      studyProgramAssocs = scheduleEntriesWithStudyProgram.flatMap(_._2)
      _ <- scheduleEntryRepository.createMany(
        scheduleEntries,
        studyProgramAssocs
      )
    } yield Ok(Json.obj("created" -> scheduleEntries.size))
  }

  private def extrapolate(
      xs: List[(ScheduleEntry, Iterable[ModuleStudyProgramScheduleEntry])],
      s: Semester
  ) = {
    val weeks = Weeks.weeksBetween(s.lectureStart, s.lectureEnd)
    val blockedDays = this.blockedDays
    val result = ListBuffer
      .empty[(ScheduleEntry, Iterable[ModuleStudyProgramScheduleEntry])]
    (0 until weeks.getWeeks).foreach { week =>
      xs.foreach { case (s, sps) =>
        val newDate = s.date.plusWeeks(week)
        if (!blockedDays.contains(newDate)) {
          val newSchedule = s.copy(id = UUID.randomUUID, date = newDate)
          val newSps = sps.map(sp => sp.copy(newSchedule.id))
          result += ((newSchedule, newSps))
        }
      }
    }
    result.toList
  }

  private def blockedDays: List[LocalDate] =
    List(
      // holidays
      LocalDate.parse("2024-05-01"),
      LocalDate.parse("2024-05-09"),
      LocalDate.parse("2024-05-20"),
      LocalDate.parse("2024-05-30"),
      // exam
      LocalDate.parse("2024-04-08"),
      LocalDate.parse("2024-04-09"),
      LocalDate.parse("2024-04-10"),
      LocalDate.parse("2024-04-11"),
      LocalDate.parse("2024-04-12"),
      LocalDate.parse("2024-07-22"),
      LocalDate.parse("2024-07-23"),
      LocalDate.parse("2024-07-24"),
      LocalDate.parse("2024-07-25"),
      LocalDate.parse("2024-07-26"),
      // other
      LocalDate.parse("2024-04-01"),
      LocalDate.parse("2024-04-02"),
      LocalDate.parse("2024-04-03"),
      LocalDate.parse("2024-04-04"),
      LocalDate.parse("2024-04-05")
    )

  private def squash(
      xs: List[ScheduleEntryProtocol]
  ) = {
    xs
      .groupBy(a => (a.date, a.room, a.course, a.moduleInStudyProgram))
      .flatMap { case ((date, room, course, moduleInStudyProgram), xs) =>
        val range = xs.sortBy(_.start).flatMap(a => List(a.start, a.end))
        val oneSlot = range.sliding(2).forall { xs =>
          val res = xs.head.compareTo(xs.last)
          res < 0 || res == 0
        }
        if (oneSlot) { // TODO only lecture
          println(s"squashing ${xs.size} entries from $course")
          List(
            ScheduleEntryProtocol(
              moduleInStudyProgram,
              course,
              room,
              date,
              range.head,
              range.last
            )
          )
        } else {
          xs
        }
      }
      .groupBy(a => (a.date, a.start, a.end, a.course, a.room))
      .map { case ((date, start, end, course, room), xs) =>
        val s = ScheduleEntry(UUID.randomUUID(), course, room, date, start, end)
        val ss = xs.map(a =>
          ModuleStudyProgramScheduleEntry(s.id, a.moduleInStudyProgram)
        )
        (s, ss)
      }
      .toList
  }

  private def printErr(s: String): Unit =
    println(Console.RED + s + Console.RESET)
}
