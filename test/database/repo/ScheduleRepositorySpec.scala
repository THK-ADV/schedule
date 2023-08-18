package database.repo

import database.repos.ScheduleEntryRepository
import database.tables._
import date.DateFormatPattern.{datePattern, timePattern}
import models._
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import suite.{AsyncSpec, DatabaseSuite, FakeApplication}

import java.sql.{Date, Time}
import java.util.UUID

final class ScheduleRepositorySpec
    extends AsyncSpec
    with GuiceOneAppPerSuite
    with FakeApplication
    with DatabaseSuite {

  val repo = app.injector.instanceOf(classOf[ScheduleEntryRepository])

  import slick.jdbc.PostgresProfile.api._

  def id = UUID.randomUUID

  def date = new Date(System.currentTimeMillis())

  def time = new Time(System.currentTimeMillis())

  def user =
    UserDbEntry("", "", "", UserStatus.Lecturer, "", None, None, timestamp, id)

  def module(user: UUID) = ModuleDbEntry(user, "", "", 0.0, "", timestamp, id)

  def submodule(module: UUID) = SubModuleDbEntry(
    module,
    "",
    "",
    0,
    0.0,
    "",
    Language.Unknown,
    Season.Unknown,
    timestamp,
    id
  )

  def course(
      user: UUID,
      semester: UUID,
      subModule: UUID,
      courseType: CourseType
  ) = CourseDbEntry(
    user,
    semester,
    subModule,
    CourseInterval.Unknown,
    courseType,
    timestamp,
    id
  )

  def semester = SemesterDbEntry("", "", date, date, date, date, timestamp, id)

  def campusEntry = CampusDbEntry("", "", timestamp, id)

  def room(campus: UUID) = RoomDbEntry(campus, "", "", timestamp, id)

  def scheduleEntry(course: UUID, room: UUID, mer: UUID) = ScheduleDbEntry(
    course,
    room,
    mer,
    date,
    time,
    time,
    ScheduleEntryStatus.Unknown,
    timestamp,
    id
  )

  def faculty = FacultyDbEntry("", "", 0, timestamp, id)

  def teachingUnit(faculty: UUID) =
    TeachingUnitDbEntry(faculty, "", "", 0, timestamp, id)

  def graduation = GraduationDbEntry("", "", timestamp, id)

  def studyProgram(teachingUnit: UUID, graduation: UUID, parent: Option[UUID]) =
    StudyProgramDBEntry(teachingUnit, graduation, "", "", parent, timestamp, id)

  def examReg(studyProgram: UUID, number: Int) =
    ExaminationRegulationDbEntry(
      studyProgram,
      number,
      date,
      None,
      timestamp,
      id
    )

  def moduleExamReg(module: UUID, examReg: UUID) =
    ModuleExaminationRegulationDbEntry(
      module,
      examReg,
      mandatory = true,
      timestamp,
      id
    )

  "A ScheduleRepository" should {
    "fetch schedule entries by courses" in {
      val u1 = user // fv
      val u2 = user // ck
      val sem = semester
      val m1 = module(u1.id) // ap1
      val m2 = module(u2.id) // ap2
      val sub1 = submodule(m1.id) // ap1
      val sub2 = submodule(m2.id) // ap2
      val c1 = course(u1.id, sem.id, sub1.id, CourseType.Lecture) // ap1 lecture
      val c2 =
        course(u1.id, sem.id, sub1.id, CourseType.Exercise) // ap1 exercise
      val c3 = course(u2.id, sem.id, sub2.id, CourseType.Lecture) // ap2 lecture
      val c4 =
        course(u2.id, sem.id, sub2.id, CourseType.Practical) // ap2 practical
      val c5 =
        course(u2.id, sem.id, sub2.id, CourseType.Exercise) // ap2 exercise
      val camp = campusEntry
      val r1 = room(camp.id)
      val fac = faculty // f10
      val tu = teachingUnit(fac.id) // inf
      val g = graduation // bachelor
      val sp0 = studyProgram(tu.id, g.id, None) // inf
      val sp1 = studyProgram(tu.id, g.id, Some(sp0.id)) // ai
      val sp2 = studyProgram(tu.id, g.id, None) // mi
      val sp3 = studyProgram(tu.id, g.id, None) // wi
      val examReg1 = examReg(sp1.id, 1)
      val examReg2 = examReg(sp2.id, 1)
      val examReg3 = examReg(sp3.id, 1)
      val examReg4 = examReg(sp0.id, 1)

      val mer1 = moduleExamReg(m1.id, examReg4.id) // ap1 in inf (ai)
      val mer2 = moduleExamReg(m1.id, examReg2.id) // ap1 in mi
      val mer3 = moduleExamReg(m1.id, examReg3.id) // ap1 in wi
      val mer4 = moduleExamReg(m2.id, examReg1.id) // ap2 in ai
      val mer5 = moduleExamReg(m2.id, examReg2.id) // ap2 in mi
      val mer6 = moduleExamReg(m2.id, examReg3.id) // ap2 in wi

      val s1 = scheduleEntry(c1.id, r1.id, mer1.id) // ap1 lecture in inf (ai)
      val s2 = scheduleEntry(c1.id, r1.id, mer2.id) // ap1 lecture in mi
      val s3 = scheduleEntry(c1.id, r1.id, mer3.id) // ap1 lecture in wi
      val s4 = scheduleEntry(c2.id, r1.id, mer1.id) // ap1 exercise in inf (ai)
      val s5 = scheduleEntry(c2.id, r1.id, mer2.id) // ap1 exercise in mi
      val s6 = scheduleEntry(c2.id, r1.id, mer3.id) // ap1 exercise in wi
      val s7 = scheduleEntry(c3.id, r1.id, mer4.id) // ap2 lecture in ai
      val s8 = scheduleEntry(c3.id, r1.id, mer5.id) // ap2 lecture in mi
      val s9 = scheduleEntry(c3.id, r1.id, mer6.id) // ap2 lecture in wi
      val s10 = scheduleEntry(c4.id, r1.id, mer4.id) // ap2 practical in ai
      val s11 = scheduleEntry(c4.id, r1.id, mer5.id) // ap2 practical in mi
      val s12 = scheduleEntry(c5.id, r1.id, mer4.id) // ap2 exercise in ai
      val s13 = scheduleEntry(c5.id, r1.id, mer5.id) // ap2 exercise in mi
      val s14 = scheduleEntry(c5.id, r1.id, mer6.id) // ap2 exercise in wi

      val res = for {
        _ <- withFreshDb(
          DBIO.seq(
            faculties += fac,
            teachingUnits += tu,
            graduations += g,
            studyPrograms ++= Seq(sp0, sp1, sp2, sp3),
            examinationRegulations ++= Seq(
              examReg1,
              examReg2,
              examReg3,
              examReg4
            ),
            users ++= Seq(u1, u2),
            modules ++= Seq(m1, m2),
            moduleExaminationRegulations ++= Seq(
              mer1,
              mer2,
              mer3,
              mer4,
              mer5,
              mer6
            ),
            submodules ++= Seq(sub1, sub2),
            semesters += sem,
            courses ++= Seq(c1, c2, c3, c4, c5),
            campus += camp,
            rooms += r1,
            schedules ++= Seq(
              s1,
              s2,
              s3,
              s4,
              s5,
              s6,
              s7,
              s8,
              s9,
              s10,
              s11,
              s12,
              s13,
              s14
            )
          )
        )
        res <- repo.list(
          Map("courses" -> Seq(c1.id.toString)),
          atomic = false
        )
      } yield res

      val scheduleEntries = await(res)
      scheduleEntries.size shouldBe 3
      compare(scheduleEntries.find(_.id == s1.id).value, s1)
      compare(scheduleEntries.find(_.id == s2.id).value, s2)
      compare(scheduleEntries.find(_.id == s3.id).value, s3)
    }
  }

  def compare(s1: Schedule, s2: ScheduleDbEntry) = {
    s1.courseId shouldBe s2.course
    s1.roomId shouldBe s2.room
    s1.moduleExaminationRegulationId shouldBe s2.moduleExaminationRegulation
    s1.date.toString(datePattern) shouldBe s2.date.toString
    s1.start.toString(timePattern) shouldBe s2.start.toString
    s1.end.toString(timePattern) shouldBe s2.end.toString
    s1.status shouldBe s2.status
  }
}
