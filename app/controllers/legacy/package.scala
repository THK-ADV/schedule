package controllers

import models._
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

import java.nio.file.{Files, Path, Paths}
import java.sql.Timestamp
import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}
import scala.jdk.CollectionConverters.ListHasAsScala
import scala.util.Try

package object legacy {
  def now: Timestamp =
    new Timestamp(System.currentTimeMillis())

  def uuid: UUID = UUID.randomUUID()

  val dateFormatter = DateTimeFormat.forPattern("dd.MM.yyyy")

  val timeFormatter = DateTimeFormat.forPattern("HH:mm")

  val engineerStudyPrograms = List(
    "Maschinenbau",
    "Automatisierungstechnik",
    "Elektronik",
    "Wirtschaftsingenieurwesen",
    "Master: Produktdesign und Prozessentwicklung",
    "Master: Automation and IT Master",
    "Master: Wirtschaftsingenieurwesen"
  )

  private def parseUser(line: String): UserJson = {
    val Array(firstname, lastname) = line.split(";")
    UserJson(
      "???",
      firstname,
      lastname,
      UserStatus.Lecturer,
      "???",
      Some("???"),
      Some("???")
    )
  }

  private def parseFaculty(line: String): FacultyJson = {
    val Array(label, abbrev, number) = line.split(";")
    FacultyJson(label, abbrev, number.toInt)
  }

  private def parseTeachingUnits(
      facultyId: UUID
  )(line: String): TeachingUnitJson = {
    val Array(label, abbrev, number) = line.split(";")
    TeachingUnitJson(facultyId, label, abbrev, number.toInt)
  }

  private def parseGraduation(line: String): GraduationJson = {
    val Array(label, abbrev) = line.split(";")
    GraduationJson(label, abbrev)
  }

  private def parseSemester(line: String): SemesterJson = {
    val Array(label, abbrev, start, end) = line.split(";")
    val startDate = LocalDate.parse(start, dateFormatter)
    val endDate = LocalDate.parse(end, dateFormatter)

    SemesterJson(
      label,
      abbrev,
      startDate,
      endDate,
      startDate,
      endDate
    )
  }

  private def parseCampus(line: String): CampusJson = {
    val Array(label, abbrev) = line.split(";")
    CampusJson(label, abbrev)
  }

  private def parseRooms(campusId: UUID)(line: String): RoomJson = {
    val Array(label, abbrev) = line.split(";")
    RoomJson(campusId, label, abbrev)
  }

  private def pathFor(file: String): Future[Path] =
    Future.fromTry(Try(Paths.get(file)))

  def primitives(
      f: FacultyJson => Future[Faculty],
      c: CampusJson => Future[Campus]
  )(implicit ctx: ExecutionContext) = {
    def parse[A](path: Path, f: String => A) =
      Files.readAllLines(path).asScala.toSeq.map(f)

    for {
      facPath <- pathFor("app/controllers/legacy/faculty.txt")
      gradsPath <- pathFor("app/controllers/legacy/graduations.txt")
      tusPath <- pathFor("app/controllers/legacy/teaching-units.txt")
      lecPath <- pathFor("app/controllers/legacy/lecturer.txt")
      semPath <- pathFor("app/controllers/legacy/semesters.txt")
      cPath <- pathFor("app/controllers/legacy/campus.txt")
      rPath <- pathFor("app/controllers/legacy/rooms.txt")
      fac <- f(parse(facPath, parseFaculty).head)
      camp <- c(parse(cPath, parseCampus).head)
    } yield {
      val tus = parse(tusPath, parseTeachingUnits(fac.id))
      val grads = parse(gradsPath, parseGraduation)
      val lec = parse(lecPath, parseUser)
      val sem = parse(semPath, parseSemester)
      val rooms = parse(rPath, parseRooms(camp.id))
      (fac, tus, grads, lec, sem, camp, rooms)
    }
  }
}
