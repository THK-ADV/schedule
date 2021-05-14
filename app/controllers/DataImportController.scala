package controllers

import database.repos.{GraduationRepository, UserRepository}
import database.tables.{GraduationDbEntry, UserDbEntry}
import date.LocalDateFormat
import models._
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import play.api.libs.json.{JsResult, _}
import play.api.mvc.{AbstractController, ControllerComponents, Result}
import service._

import java.sql.Timestamp
import java.util.UUID
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class DataImportController @Inject() (
    cc: ControllerComponents,
    implicit val ctx: ExecutionContext,
    val userRepo: UserRepository,
    val graduationRepo: GraduationRepository,
    val graduationService: GraduationService,
    val teachingUnitService: TeachingUnitService,
    val facultyService: FacultyService,
    val semesterService: SemesterService,
    val roomService: RoomService
) extends AbstractController(cc)
    with LocalDateFormat {

  val dayPattern = DateTimeFormat.forPattern("dd.MM.yy")

  def primitives() = Action.async { r =>
    val user = UserDbEntry(
      "???",
      "???",
      "lecturer",
      "???",
      Some("???"),
      Some("?.?"),
      now,
      UUID.randomUUID()
    )

    val graduations = List(
      GraduationDbEntry("Bachelor", "BA", now, UUID.randomUUID()),
      GraduationDbEntry("Master", "MA", now, UUID.randomUUID())
    )

    for {
      gs <- Future.sequence(graduations.map(a => graduationRepo.create(a, Nil)))
      u <- userRepo.create(user, Nil)
    } yield Ok(
      Json.obj(
        "user" -> u,
        "graduations" -> gs
      )
    )
  }

  def faculties() = Action(parse.text).async { r =>
    createMany(
      parseCSV(r.body, FacultyJson.format) {
        case ("number", value)       => JsNumber(value.toInt)
        case ("label", value)        => JsString(value)
        case ("abbreviation", value) => JsString(value)
      },
      facultyService.create
    )
  }

  def teachingUnits() = Action(parse.text).async { r =>
    createMany(
      parseCSV(r.body, TeachingUnitJson.format) {
        case ("number", value)       => JsNumber(value.toInt)
        case ("label", value)        => JsString(value)
        case ("abbreviation", value) => JsString(value)
      },
      teachingUnitService.create
    )
  }

  def semesters() = Action(parse.text).async { r =>
    createMany(
      parseCSV(r.body, SemesterJson.format) {
        case ("label", value)        => JsString(value)
        case ("abbreviation", value) => JsString(value)
        case (_, value) => // camcel case rename
          localDateFormat.writes(LocalDate.parse(value, dayPattern))
      },
      semesterService.create
    )
  }

  def rooms() = Action(parse.text).async { r =>
    createMany(
      parseCSV(r.body, RoomJson.format) {
        case ("label", value)        => JsString(value) // encoding issue
        case ("abbreviation", value) => JsString(value) // encoding issue
      },
      roomService.create
    )
  }

  def studyPrograms() = Action(parse.text).async { r =>
    for {
      graduations <- graduationService.all(false)
      tus <- teachingUnitService.all(false)
    } yield {
      toResult(parseCSVWithCustomKeys(r.body, StudyProgramJson.format) {
        case ("label", value)        => "label" -> JsString(value)
        case ("abbreviation", value) => "abbreviation" -> JsString(value)
        case ("graduation", value) =>
          "graduation" -> toJsonID(graduations.find(_.label == value))
        case ("teaching_unit_FK", value) =>
          "teachingUnit" -> toJsonID(
            tus.find(_.abbreviation == value)
          ) // rename ING_FK to ING
      })
    }
  }

  private def toJsonID[A <: UniqueEntity](a: Option[A]) = JsString(
    a.map(_.id.toString).get
  )

  private def toResult[A](json: JsResult[(JsArray, Seq[A])]) = json match {
    case JsSuccess((json, protocols), _) =>
      println(protocols)
      Ok(json)
    case JsError(errors) =>
      BadRequest(JsString(errors.toString()))
  }

  private def map[A, B](
      json: JsResult[(JsArray, Seq[A])]
  )(f: Seq[A] => Future[B]): Future[Result] = json match {
    case JsSuccess((json, protocols), _) =>
      f(protocols).map(_ => Ok(json))
    case JsError(errors) =>
      Future.successful(BadRequest(JsString(errors.toString())))
  }

  private def createMany[A, B](
      json: JsResult[(JsArray, Seq[A])],
      f: A => Future[B]
  ): Future[Result] =
    map(json)(xs => Future.sequence(xs.map(f)))

  private def now = new Timestamp(System.currentTimeMillis())

  private def toProtocol[A](array: JsArray, reads: Reads[A]): JsResult[Seq[A]] =
    Reads.list(reads).reads(array)

  private def parseCSV[A](body: String, reads: Reads[A])(
      f: (String, String) => JsValue
  ): JsResult[(JsArray, Seq[A])] =
    parseCSVWithCustomKeys(body, reads)((k, v) => k -> f(k, v))

  private def parseCSVWithCustomKeys[A](body: String, reads: Reads[A])(
      f: (String, String) => (String, JsValue)
  ): JsResult[(JsArray, Seq[A])] = {
    val all = body.linesIterator.toVector
    val header = all.head.split(";")
    val rows = all.drop(1)

    val res = rows.map { row =>
      val cols = row.split(";")
      header.zip(cols).foldLeft(Json.obj()) {
        case (json, (key, _)) if key == "id" =>
          json
        case (json, (key, value)) =>
          json.+(f(key, value))
      }
    }

    val array = JsArray(res)
    println(array)
    toProtocol(array, reads).map(seq => (array, seq))
  }
}
