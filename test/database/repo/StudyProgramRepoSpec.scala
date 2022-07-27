package database.repo

import database.repos.StudyProgramRepository
import database.tables._
import models.StudyProgram.StudyProgramAtom
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import suite.{AsyncSpec, DatabaseSuite, FakeApplication}

class StudyProgramRepoSpec
    extends AsyncSpec
    with GuiceOneAppPerSuite
    with FakeApplication
    with DatabaseSuite {
  val repo = app.injector.instanceOf(classOf[StudyProgramRepository])
  import slick.jdbc.PostgresProfile.api._

  "A StudyProgramRepo" should {
    "fetch study programs with no parents" in {
      val fac = FacultyDbEntry("", "", 0, timestamp, uuid)
      val tu = TeachingUnitDbEntry(fac.id, "", "", 0, timestamp, uuid)
      val g = GraduationDbEntry("", "", timestamp, uuid)
      val sps = List(
        StudyProgramDBEntry(tu.id, g.id, "sp1", "sp1", None, timestamp, uuid),
        StudyProgramDBEntry(tu.id, g.id, "sp2", "sp2", None, timestamp, uuid),
        StudyProgramDBEntry(tu.id, g.id, "sp3", "sp3", None, timestamp, uuid),
        StudyProgramDBEntry(tu.id, g.id, "sp4", "sp4", None, timestamp, uuid)
      )

      val res = for {
        _ <- withSetup(
          faculties += fac,
          teachingUnits += tu,
          graduations += g,
          studyPrograms ++= sps
        )
        sps <- repo.list(Map.empty, atomic = true)
      } yield sps

      await(res).forall(_.parentId.isEmpty) shouldBe true
    }

    "fetch study programs with parents" in {
      val fac1 = FacultyDbEntry("", "", 1, timestamp, uuid)
      val fac2 = FacultyDbEntry("", "", 2, timestamp, uuid)
      val tu1 = TeachingUnitDbEntry(fac1.id, "", "", 1, timestamp, uuid)
      val tu2 = TeachingUnitDbEntry(fac2.id, "", "", 2, timestamp, uuid)
      val g = GraduationDbEntry("", "", timestamp, uuid)
      val sp1 =
        StudyProgramDBEntry(tu2.id, g.id, "sp1", "sp1", None, timestamp, uuid)
      val sp2 =
        StudyProgramDBEntry(
          tu1.id,
          g.id,
          "sp2",
          "sp2",
          Some(sp1.id),
          timestamp,
          uuid
        )
      val sp3 =
        StudyProgramDBEntry(
          tu1.id,
          g.id,
          "sp3",
          "sp3",
          Some(sp2.id),
          timestamp,
          uuid
        )
      val sp4 =
        StudyProgramDBEntry(tu1.id, g.id, "sp4", "sp4", None, timestamp, uuid)
      val sps = List(sp1, sp2, sp3, sp4)

      val res = for {
        _ <- withSetup(
          faculties ++= List(fac1, fac2),
          teachingUnits ++= List(tu1, tu2),
          graduations += g,
          studyPrograms ++= sps
        )
        sps <- repo.list(Map.empty, atomic = true)
      } yield sps

      val Seq(s1, s2, s3, s4) = await(res).map(_.asInstanceOf[StudyProgramAtom])
      s1.parent.isEmpty shouldBe true
      s2.parent.value shouldBe s1
      s3.parent.value shouldBe s2
      s3.parent.value.parent.value shouldBe s1
      s4.parent.isEmpty shouldBe true
    }
  }
}
