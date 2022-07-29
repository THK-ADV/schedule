package controllers.legacy

import json.{FacultyFormat, ScheduleFormat}
import models.Course.{CourseAtom, CourseDefault}
import models.ExaminationRegulation.{
  ExaminationRegulationAtom,
  ExaminationRegulationDefault
}
import models.Module.{ModuleAtom, ModuleDefault}
import models.ModuleExaminationRegulation.{
  ModuleExaminationRegulationAtom,
  ModuleExaminationRegulationDefault
}
import models.Room.RoomDefault
import models.Schedule.{ScheduleAtom, ScheduleDefault}
import models.StudyProgram.{StudyProgramAtom, StudyProgramDefault}
import models.SubModule.{SubModuleAtom, SubModuleDefault}
import models.User.Lecturer
import models._
import org.joda.time.{LocalDate, LocalTime}
import play.api.libs.json.{JsString, Json}
import play.api.mvc.{AbstractController, ControllerComponents}
import service._

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}
import java.util.stream
import javax.inject.{Inject, Singleton}
import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try
import scala.util.control.NonFatal

trait Creation {
  def createStudyPrograms(sps: Seq[StudyProgramJson]): Future[Seq[StudyProgram]]
  def createExams(
      exams: Seq[ExaminationRegulationJson]
  ): Future[Seq[ExaminationRegulation]]
  def createModules(modules: List[ModuleJson]): Future[Seq[Module]]
  def createModulesInPo(
      modulesInPO: Seq[ModuleExaminationRegulationJson]
  ): Future[Seq[ModuleExaminationRegulation]]
  def createSubModules(subModules: Seq[SubModuleJson]): Future[Seq[SubModule]]
  def createUsers(users: Seq[UserJson]): Future[Seq[User]]
  def createTeachingUnits(
      tus: Seq[TeachingUnitJson]
  ): Future[Seq[TeachingUnit]]
  def createGraduations(gs: Seq[GraduationJson]): Future[Seq[Graduation]]
  def createFaculty(f: FacultyJson): Future[Seq[Faculty]]
  def createSemester(xs: Seq[SemesterJson]): Future[Seq[Semester]]
  def createCourses(xs: Seq[CourseJson]): Future[Seq[Course]]
  def createCampus(c: CampusJson): Future[Seq[Campus]]
  def createRooms(xs: Seq[RoomJson]): Future[Seq[Room]]
  def createSchedules(xs: Seq[ScheduleJson]): Future[Seq[Schedule]]
}

@Singleton
final class MockCreation extends Creation {
  def createStudyPrograms(
      sps: Seq[StudyProgramJson]
  ): Future[Seq[StudyProgram]] =
    Future.successful(
      sps
        .map(s =>
          StudyProgramDefault(
            s.teachingUnit,
            s.graduation,
            s.label,
            s.abbreviation,
            None,
            uuid
          )
        )
    )

  def createExams(
      exams: Seq[ExaminationRegulationJson]
  ): Future[Seq[ExaminationRegulation]] =
    Future.successful(
      exams
        .map(s =>
          ExaminationRegulationDefault(
            s.studyProgram,
            s.number,
            s.start,
            s.end,
            uuid
          )
        )
    )

  def createModules(modules: List[ModuleJson]): Future[Seq[Module]] =
    Future.successful(
      modules
        .map(m =>
          ModuleDefault(
            m.courseManager,
            m.label,
            m.abbreviation,
            m.credits,
            m.descriptionUrl,
            uuid
          )
        )
    )

  def createModulesInPo(
      modulesInPO: Seq[ModuleExaminationRegulationJson]
  ): Future[Seq[ModuleExaminationRegulation]] =
    Future.successful(
      modulesInPO
        .map(a =>
          ModuleExaminationRegulationDefault(
            a.module,
            a.examinationRegulation,
            a.mandatory,
            uuid
          )
        )
    )

  def createSubModules(subModules: Seq[SubModuleJson]): Future[Seq[SubModule]] =
    Future.successful(
      subModules
        .map(a =>
          SubModuleDefault(
            a.module,
            a.label,
            a.abbreviation,
            a.recommendedSemester,
            a.credits,
            a.descriptionUrl,
            a.language,
            a.season,
            uuid
          )
        )
    )

  override def createUsers(users: Seq[UserJson]): Future[Seq[User]] =
    Future.successful(
      users
        .map(u =>
          Lecturer(
            u.username,
            u.firstname,
            u.lastname,
            u.email,
            u.title.get,
            u.initials.get,
            uuid
          )
        )
    )

  override def createTeachingUnits(
      tus: Seq[TeachingUnitJson]
  ): Future[Seq[TeachingUnit]] =
    Future.successful(
      tus
        .map(t =>
          TeachingUnit(t.faculty, t.label, t.abbreviation, t.number, uuid)
        )
    )

  override def createGraduations(gs: Seq[GraduationJson]) =
    Future.successful(
      gs.map(g => Graduation(g.label, g.abbreviation, uuid))
    )

  override def createFaculty(f: FacultyJson) =
    Future.successful(Seq(Faculty(f.label, f.abbreviation, f.number, uuid)))

  override def createSemester(xs: Seq[SemesterJson]) =
    Future.successful(
      xs.map(s =>
        Semester(
          s.label,
          s.abbreviation,
          s.start,
          s.end,
          s.lectureStart,
          s.lectureEnd,
          uuid
        )
      )
    )

  override def createCourses(xs: Seq[CourseJson]) =
    Future.successful(
      xs.map(c =>
        CourseDefault(
          c.lecturer,
          c.semester,
          c.subModule,
          c.interval,
          c.courseType,
          uuid
        )
      )
    )

  override def createCampus(c: CampusJson) =
    Future.successful(Seq(Campus(c.label, c.abbreviation, uuid)))

  override def createRooms(xs: Seq[RoomJson]) =
    Future.successful(
      xs.map(r => RoomDefault(r.campus, r.label, r.abbreviation, uuid))
    )

  override def createSchedules(xs: Seq[ScheduleJson]) =
    Future.successful(
      xs.map(s =>
        ScheduleDefault(
          s.course,
          s.room,
          s.moduleExaminationRegulation,
          s.date,
          s.start,
          s.end,
          ScheduleEntryStatus.Draft,
          uuid
        )
      )
    )
}

@Singleton
final class LiveCreation @Inject() (
    val studyProgramService: StudyProgramService,
    val examinationRegulationService: ExaminationRegulationService,
    val moduleService: ModuleService,
    val subModuleService: SubModuleService,
    val moduleExaminationRegulationService: ModuleExaminationRegulationService,
    val userService: UserService,
    val facultyService: FacultyService,
    val graduationService: GraduationService,
    val semesterService: SemesterService,
    val teachingUnitService: TeachingUnitService,
    val courseService: CourseService,
    val campusService: CampusService,
    val roomService: RoomService,
    val scheduleService: ScheduleService,
    implicit val ctx: ExecutionContext
) extends Creation {

  override def createStudyPrograms(sps: Seq[StudyProgramJson]) =
    for {
      _ <- Future.sequence(sps.map(studyProgramService.create))
      all <- studyProgramService.all(true)
    } yield all

  override def createExams(exams: Seq[ExaminationRegulationJson]) =
    for {
      _ <- Future.sequence(exams.map(examinationRegulationService.create))
      all <- examinationRegulationService.all(true)
    } yield all

  override def createModules(modules: List[ModuleJson]) =
    for {
      _ <- Future.sequence(modules.map(moduleService.create))
      all <- moduleService.all(true)
    } yield all

  override def createModulesInPo(
      modulesInPO: Seq[ModuleExaminationRegulationJson]
  ) =
    for {
      _ <- Future.sequence(
        modulesInPO.map(moduleExaminationRegulationService.create)
      )
      all <- moduleExaminationRegulationService.all(true)
    } yield all

  override def createSubModules(subModules: Seq[SubModuleJson]) =
    for {
      _ <- Future.sequence(subModules.map(subModuleService.create))
      all <- subModuleService.all(true)
    } yield all

  override def createUsers(users: Seq[UserJson]) =
    for {
      _ <- Future.sequence(users.map(userService.create))
      all <- userService.all(true)
    } yield all

  override def createTeachingUnits(tus: Seq[TeachingUnitJson]) =
    for {
      _ <- Future.sequence(tus.map(teachingUnitService.create))
      all <- teachingUnitService.all(true)
    } yield all

  override def createGraduations(gs: Seq[GraduationJson]) =
    for {
      _ <- Future.sequence(gs.map(graduationService.create))
      all <- graduationService.all(true)
    } yield all

  override def createFaculty(f: FacultyJson) =
    facultyService.create(f).flatMap(_ => facultyService.all(true))

  override def createSemester(s: Seq[SemesterJson]) =
    for {
      _ <- Future.sequence(s.map(semesterService.create))
      all <- semesterService.all(true)
    } yield all

  override def createCourses(xs: Seq[CourseJson]) =
    for {
      _ <- Future.sequence(xs.map(courseService.create))
      all <- courseService.all(true)
    } yield all

  override def createCampus(c: CampusJson) =
    campusService.create(c).flatMap(_ => campusService.all(true))

  override def createRooms(xs: Seq[RoomJson]) =
    for {
      _ <- Future.sequence(xs.map(roomService.create))
      all <- roomService.all(true)
    } yield all

  override def createSchedules(xs: Seq[ScheduleJson]) =
    for {
      _ <- Future.sequence(xs.map(scheduleService.create))
      all <- scheduleService.all(true)
    } yield all
}

@Singleton
class LegacyDbImportController @Inject() (
    cc: ControllerComponents,
    creation: LiveCreation,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with ScheduleFormat
    with FacultyFormat {

  case class CSVData(
      fachkuerzel: String,
      kurzbez: String,
      bezeichnung: String,
      dozentenkuerzel: String,
      nachname: String,
      vorname: String,
      fachtyp: String,
      sg_kz: String,
      sr_kz: String,
      zuordnung_curriculum: String,
      semester: String,
      bezeichnung1: String,
      wochentag: String,
      time: String,
      raum_kz: String,
      tu: String
  )

  def tap[A](a: A): A = {
    println(a)
    a
  }

  def parseLine(line: String): Option[CSVData] = {
    val data = line.split(";").map(_.replace("\"", ""))
    if (data.length != 16) {
      println(s"line does not have all fields set: $line")
      return None
    }
    Some(
      CSVData(
        data(0),
        data(1),
        data(2),
        data(3),
        data(4),
        data(5),
        data(6),
        data(7),
        data(8),
        data(9),
        data(10),
        data(11),
        data(12),
        data(13),
        data(14),
        data(15)
      )
    )
  }

  def toList[A](res: stream.Stream[Option[A]]): List[A] = {
    val list = ListBuffer[A]()
    res.forEach { a =>
      if (a.isDefined)
        list += a.get
    }
    list.toList
  }

  type ModuleType =
    (String, String, String, String, String, String, String)

  private def isEmpty(s: String) = s == "\"\"" || s.isEmpty

  def go(list: List[CSVData]) = {
    val studyPrograms = ListBuffer[(String, String, String)]()
    val modules = ListBuffer[ModuleType]()
    val courses = ListBuffer[(String, String, String)]()
    val schedule =
      ListBuffer[(String, String, String, String, String, String)]()

    list
      .foreach { d =>
        if (
          !isEmpty(d.bezeichnung) &&
          !isEmpty(d.kurzbez) &&
          !isEmpty(d.nachname) &&
          !isEmpty(d.vorname) &&
          !isEmpty(d.semester) &&
          !isEmpty(d.bezeichnung1) &&
          !isEmpty(d.sg_kz)
        ) {
          studyPrograms += Tuple3(d.bezeichnung1, d.sg_kz, d.tu)
          modules += Tuple7(
            d.bezeichnung,
            d.kurzbez,
            d.nachname,
            d.vorname,
            d.semester,
            d.bezeichnung1,
            d.sg_kz
          )
        }
        if (
          !isEmpty(d.bezeichnung) &&
          !isEmpty(d.kurzbez) &&
          !isEmpty(d.fachtyp) &&
          !isEmpty(d.wochentag) &&
          !isEmpty(d.time) &&
          !isEmpty(d.raum_kz)
        ) {
          courses += Tuple3(d.bezeichnung, d.kurzbez, d.fachtyp)
          schedule += Tuple6(
            d.bezeichnung,
            d.kurzbez,
            d.fachtyp,
            d.wochentag,
            d.time,
            d.raum_kz
          )
        }
      }

    val moduleMapping: Set[
      ((String, String, String, String, String), Set[(String, String)])
    ] =
      modules.toSet
        .groupBy[Tuple5[String, String, String, String, String]](a =>
          Tuple5(a._1, a._2, a._3, a._4, a._5)
        )
        .map { case (module, studyPaths) =>
          module -> studyPaths.map(a => a._6 -> a._7)
        }
        .toSet

    (
      studyPrograms.toSet,
      moduleMapping,
      courses.toSet,
      schedule.toSet
    )
  }

  def makeStudyPrograms(
      tus: Seq[TeachingUnit],
      grads: Seq[Graduation],
      studyPrograms: Set[(String, String, String)]
  ) =
    studyPrograms.map { case (label, abbrev, tu) =>
      val gID = grads.find(_.abbreviation.head == abbrev.last).get.id
      val tuID = tus.find(_.abbreviation.equalsIgnoreCase(tu)).get.id
      StudyProgramJson(tuID, gID, abbrev, label, None)
    }.toSeq

  def makeExams(sps: Seq[StudyProgram]) = {
    val defaultStart = LocalDate.parse("2021-01-01")
    val defaultPONumber = 1

    sps.map(sp =>
      ExaminationRegulationJson(sp.id, defaultPONumber, defaultStart, None)
    )
  }

  def makeModules(
      users: Seq[User],
      modules: Set[(String, String, String, String, String)]
  ) =
    modules.foldLeft(List.empty[ModuleJson]) {
      case (xs, (label, abbrev, lastname, firstname, _)) =>
        val cm = (users.find(u =>
          u.firstname == firstname && u.lastname == lastname
        ) orElse users.find(u =>
          u.firstname == "???" && u.lastname == "???"
        )).get

        if (xs.exists(m => m.label == label && m.abbreviation == abbrev)) xs
        else ModuleJson(cm.id, label, abbrev, -1.0, "???") :: xs
    }

  def makeModulesInPO(
      modules: Seq[Module],
      exams: Seq[ExaminationRegulation],
      studyPrograms: Seq[StudyProgram],
      mapping: Set[
        (
            (String, String, String, String, String),
            Set[(String, String)]
        )
      ]
  ) = {
    val defaultMandatory = true

    modules.flatMap { m =>
      val mps = mapping
        .find(a => a._1._1 == m.label && a._1._2 == m.abbreviation)
        .get
        ._2
      val sps = studyPrograms.filter(a =>
        mps.exists(b => b._1 == a.label && b._2 == a.abbreviation)
      )
      val exs = exams.filter(a => sps.exists(_.id == a.studyProgramId))
      exs.map(e =>
        ModuleExaminationRegulationJson(m.id, e.id, defaultMandatory)
      )
    }
  }

  def makeSubModules(
      modules: Seq[Module],
      mapping: Set[
        (
            (String, String, String, String, String),
            Set[(String, String)]
        )
      ]
  ) = {
    def inferSemester(s: String): Int = {
      s.map(_.toString).flatMap(_.toIntOption).headOption orElse
        Option.when(s.startsWith("SS"))(2) orElse
        Option.when(s.startsWith("WS"))(1) getOrElse
        -1
    }

    def inferSeason(s: String): Season = {
      def liftToInts: List[Int] = {
        val ints = ListBuffer[Int]()
        if (s.contains("SS")) ints += 2
        if (s.contains("WS")) ints += 1
        ints.toList
      }

      def go(ints: List[Int]): Set[Season] =
        ints.map {
          case 1 | 3 | 5 => Season.WiSe
          case 2 | 4 | 6 => Season.SoSe
        }.toSet

      def parseInts = s.map(_.toString).flatMap(_.toIntOption).toList

      val seasons = {
        val s1 = go(parseInts)
        if (s1.nonEmpty) s1
        else go(liftToInts)
      }

      if (seasons.size == 2) Season.SoSe_WiSe
      else if (seasons.size == 1) seasons.head
      else Season.Unknown
    }

    val defaultCredits = -1
    val defaultLang = Language.DE

    modules.map { m =>
      val semester = mapping
        .find(a => a._1._1 == m.label && a._1._2 == m.abbreviation)
        .get
        ._1
        ._5

      SubModuleJson(
        m.id,
        m.label,
        m.abbreviation,
        inferSemester(semester),
        defaultCredits,
        "???",
        defaultLang,
        inferSeason(semester)
      )
    }
  }

  def makeCourses(
      subModules: Seq[SubModule],
      modules: Seq[Module],
      semester: Semester,
      mapping: Set[(String, String, String)]
  ) = {
    def toCourseType(ct: String): CourseType = ct match {
      case "Vorlesung" => CourseType.Lecture
      case "Seminar"   => CourseType.Seminar
      case "Übung"     => CourseType.Exercise
      case "Praktikum" => CourseType.Practical
      case "Tutorium"  => CourseType.Tutorial
      case _           => CourseType.Unknown
    }

    subModules.flatMap { s =>
      val m = modules.find(_.id == s.moduleId).get
      val courseTypes = mapping
        .filter(a => a._1 == m.label && a._2 == m.abbreviation)
        .map(_._3)
      courseTypes.map(ct =>
        CourseJson(
          m.courseManagerId,
          semester.id,
          s.id,
          CourseInterval.Regularly,
          toCourseType(ct)
        )
      )
    }
  }

  def makeSchedule(
      courses: Seq[Course],
      rooms: Seq[Room],
      modulesInPO: Seq[ModuleExaminationRegulation],
      subModules: Seq[SubModule],
      modules: Seq[Module],
      semester: Semester,
      mappings: Set[(String, String, String, String, String, String)]
  ) = {
    def date(weekDayIndex: Int): LocalDate =
      semester.lectureStart.withDayOfWeek(weekDayIndex)

    def start(time: String): LocalTime =
      LocalTime.parse(s"$time:00", timeFormatter)

    def end(start: LocalTime): LocalTime =
      start.plusHours(1)

    val defaultRoom = rooms.find(_.abbreviation == "???").get
    courses.flatMap { c =>
      val sms = subModules.filter(_.id == c.subModuleId)
      val ms = modules.filter(a => sms.exists(_.moduleId == a.id))
      val mpos = modulesInPO.filter(a => sms.exists(_.moduleId == a.moduleId))

      val appInfo = mappings
        .filter(a => ms.exists(m => m.label == a._1 && m.abbreviation == a._2))
        .map(a => Tuple3(a._4, a._5, a._6))

      mpos.flatMap { mpo =>
        appInfo.map { case (index, time, room) =>
          val r = rooms.find(_.abbreviation == room) getOrElse defaultRoom
          val s = start(time)
          val e = end(s)
          ScheduleJson(
            c.id,
            r.id,
            mpo.id,
            date(index.toInt),
            s,
            e,
            ScheduleEntryStatus.Draft
          )
        }
      }
    }
  }

  def importFromCSV() = Action(parse.temporaryFile).async { r =>
    val res = for {
      prims <- primitives(
        f => creation.createFaculty(f).map(_.head),
        c => creation.createCampus(c).map(_.head)
      )
      tus <- creation.createTeachingUnits(prims._2)
      grads <- creation.createGraduations(prims._3)
      users <- creation.createUsers(prims._4)
      semester <- creation.createSemester(prims._5)
      rms <- creation.createRooms(prims._7)
      csvData = toList(
        Files
          .lines(r.body.path)
          .skip(1)
          .map(parseLine)
          .filter(_.isDefined)
      )
      (studyPrograms, modules, courses, schedule) = go(csvData)
      sps <- creation.createStudyPrograms(
        makeStudyPrograms(tus, grads, studyPrograms)
      )
      exams <- creation.createExams(makeExams(sps))
      ms <- creation.createModules(makeModules(users, modules.map(_._1)))
      mpos <- creation.createModulesInPo(
        makeModulesInPO(ms, exams, sps, modules)
      )
      sms <- creation.createSubModules(makeSubModules(ms, modules))
      cs <- creation.createCourses(
        makeCourses(sms, ms, semester.head, courses)
      )
      scheds <- creation.createSchedules(
        makeSchedule(cs, rms, mpos, sms, ms, semester.head, schedule)
      )
      _ <- write(
        List(
          logStudyPrograms(sps, exams) -> "Study Programs & Exams",
          logModules(ms, sms, mpos, cs) -> "Modules, SubModules, POs, Courses",
          logSchedules(scheds) -> "Schedules"
        )
      )
    } yield {
      Json.obj(
        "faculty" -> prims._1,
        "campus" -> prims._6,
        "rooms" -> rms,
        "teaching_units" -> tus,
        "graduations" -> grads,
        "users" -> users,
        "semester" -> semester,
        "studyPrograms" -> sps,
        "exanimationRegulations" -> exams,
        "modules" -> ms,
        "module-in-pos" -> mpos,
        "subModules" -> sms,
        "courses" -> cs,
        "schedules" -> scheds
      )
    }

    res.map(a => Ok(a)).recover { case NonFatal(e) =>
      BadRequest(JsString(e.getMessage))
    }
  }

  private def write(xs: List[(Seq[String], String)]) =
    Future.fromTry(
      Try(
        Files.write(
          Paths.get("app/controllers/legacy/output.txt"),
          xs
            .map(s =>
              s"${s._2} (${s._1.size})" + s._1.mkString("\n") + "\n===="
            )
            .mkString("\n\n")
            .getBytes(StandardCharsets.UTF_8)
        )
      )
    )

  private def logStudyPrograms(
      sps: Seq[StudyProgram],
      exams: Seq[ExaminationRegulation]
  ) = {
    def go(e: ExaminationRegulationAtom) =
      s"\tnumber: ${e.number}"

    exams
      .groupBy(_.studyProgramId)
      .map { case (sid, xs) =>
        val exms = xs.map(_.asInstanceOf[ExaminationRegulationAtom])
        val sp = sps.find(_.id == sid).get.asInstanceOf[StudyProgramAtom]

        s"""
             |${sp.label} (${sp.abbreviation})
             |exams:
             |${exms.map(go).mkString("\n")}""".stripMargin
      }
      .toSeq
  }

  private def logModules(
      modules: Seq[Module],
      subModules: Seq[SubModule],
      moduleInPO: Seq[ModuleExaminationRegulation],
      courses: Seq[Course]
  ) = {
    def go(sm: SubModuleAtom, cs: Seq[CourseAtom]) = {
      val sStr =
        s"\t${sm.label} (${sm.abbreviation}) ${sm.recommendedSemester}/${sm.season}/${sm.language}"
      val cStr = cs.map(_.courseType.toString).mkString(",")
      s"$sStr $cStr"
    }

    def go2(e: ModuleExaminationRegulationAtom) = {
      val er = e.examinationRegulation
      val sp = er.studyProgram
      s"\t${sp.label} (${sp.abbreviation}) ${er.number}"
    }

    subModules
      .groupBy(_.moduleId)
      .map { case (mid, xs) =>
        val sms = xs.map(_.asInstanceOf[SubModuleAtom])
        val m = modules.find(_.id == mid).get.asInstanceOf[ModuleAtom]
        val sps = moduleInPO
          .filter(_.moduleId == m.id)
          .map(_.asInstanceOf[ModuleExaminationRegulationAtom])
        val cm = m.courseManager

        s"""
          |${m.label} (${m.abbreviation}) bei ${cm.firstname} ${cm.lastname}
          |subModule & courses:
          |${sms
          .map(s =>
            go(
              s,
              courses
                .filter(_.subModuleId == s.id)
                .map(_.asInstanceOf[CourseAtom])
            )
          )
          .mkString("\n")}
          |module in PO:
          |${sps.map(go2).mkString("\n")}""".stripMargin
      }
      .toSeq
  }

  private def showTime(t: LocalTime) = t.toString("HH:mm")

  private def showDate(d: LocalDate) = d.toString("E., dd.MM.yyyy")

  private def showCourseType(c: CourseType) = c match {
    case CourseType.Lecture   => "V"
    case CourseType.Practical => "P"
    case CourseType.Exercise  => "Ü"
    case CourseType.Tutorial  => "T"
    case CourseType.Seminar   => "S"
    case CourseType.Unknown   => "???"
  }

  private def logSchedules(schedules: Seq[Schedule]) = {

    def go(s: ScheduleAtom): String = {
      val room = s"(${s.room.abbreviation})"
      val courseType = showCourseType(s.course.courseType)
      val time = s"${showTime(s.start)}-${showTime(s.end)}"
      val sp =
        s.moduleExaminationRegulation.examinationRegulation.studyProgram.label
      s"\t$time $sp $courseType $room"
    }

    schedules
      .sortBy(_.date)
      .groupBy(_.date)
      .map { case (date, xs) =>
        val res = xs
          .sortBy(_.start)
          .map(_.asInstanceOf[ScheduleAtom])
          .groupBy(_.moduleExaminationRegulation.module.id)
          .map { case (_, ys) =>
            val merged = ys
              .groupBy(
                _.moduleExaminationRegulation.examinationRegulation.studyProgram.id
              )
              .flatMap { case (_, zs) =>
                val ss = ListBuffer[ListBuffer[ScheduleAtom]]()

                zs.sortBy(_.start).foreach { z =>
                  val i =
                    ss.indexWhere(_.exists(_.start == z.start.minusHours(1)))
                  if (i == -1) {
                    ss += ListBuffer(z)
                  } else {
                    ss(i) += z
                  }
                }

                ss.map { ws =>
                  val min = ws.minBy(_.start)
                  val max = ws.maxBy(_.end)
                  min.copy(end = max.end)
                }.toList
              }
            val m = merged.head.moduleExaminationRegulation.module
            s"${m.label} (${m.abbreviation})\n${merged.map(go).mkString("\n")}"
          }
        s"\n${showDate(date)}\n${res.mkString("\n")}"
      }
      .toSeq
  }
}
