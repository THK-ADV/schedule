package controllers

import database.repos._
import database.tables._
import date.LocalDateFormat
import models._
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import play.api.libs.json.{JsResult, _}
import play.api.mvc.{AbstractController, ControllerComponents, Result}
import service._

import java.sql.{Date, Timestamp}
import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DataImportController @Inject() (
    cc: ControllerComponents,
    implicit val ctx: ExecutionContext,
    val userRepo: UserRepository,
    val graduationRepo: GraduationRepository,
    val graduationService: GraduationService,
    val teachingUnitService: TeachingUnitService,
    val facultyService: FacultyService,
    val semesterService: SemesterService,
    val roomService: RoomService,
    val examinationRegulationService: ExaminationRegulationService,
    val userService: UserService,
    val teachingUnitRepository: TeachingUnitRepository,
    val studyProgramRepository: StudyProgramRepository,
    val studyProgramService: StudyProgramService,
    val examinationRegulationRepository: ExaminationRegulationRepository,
    val moduleService: ModuleService,
    val facultyRepository: FacultyRepository
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

    val fac = FacultyDbEntry("???", "???", -1, now, UUID.randomUUID())
    val tu =
      TeachingUnitDbEntry(fac.id, "???", "???", -1, now, UUID.randomUUID())
    val sp = StudyProgramDBEntry(
      tu.id,
      graduations.head.id,
      "???",
      "???",
      now,
      UUID.randomUUID()
    )
    val er = ExaminationRegulationDbEntry(
      sp.id,
      "???",
      "???",
      Date.valueOf("2021-01-01"),
      Date.valueOf("2021-12-31"),
      now,
      UUID.randomUUID()
    )

    for {
      gs <- Future.sequence(graduations.map(a => graduationRepo.create(a, Nil)))
      u <- userRepo.create(user, Nil)
      fac <- facultyRepository.create(fac, Nil)
      tu <- teachingUnitRepository.create(tu, Nil)
      sp <- studyProgramRepository.create(sp, Nil)
      er <- examinationRegulationRepository.create(er, Nil)
    } yield Ok(
      Json.obj(
        "user" -> u,
        "graduations" -> gs,
        "faculty" -> fac,
        "teachingUnit" -> tu,
        "studyProgram" -> sp,
        "examinationRegulation" -> er
      )
    )
  }

  def faculties() = textParsingAction.async { r =>
    createMany(
      parseCSV(r.body, FacultyJson.format) {
        case ("number", value)       => JsNumber(value.toInt)
        case ("label", value)        => JsString(value)
        case ("abbreviation", value) => JsString(value)
      },
      facultyService.create
    )
  }

  def teachingUnits() = textParsingAction.async { r =>
    createMany(
      parseCSV(r.body, TeachingUnitJson.format) {
        case ("number", value)       => JsNumber(value.toInt)
        case ("label", value)        => JsString(value)
        case ("abbreviation", value) => JsString(value)
      },
      teachingUnitService.create
    )
  }

  def semesters() = textParsingAction.async { r =>
    createMany(
      parseCSV(r.body, SemesterJson.format) {
        case ("label", value)        => JsString(value)
        case ("abbreviation", value) => JsString(value)
        case (_, value) => // camel case rename
          localDateFormat.writes(LocalDate.parse(value, dayPattern))
      },
      semesterService.create
    )
  }

  def rooms() = textParsingAction.async { r =>
    createMany(
      parseCSV(
        r.body,
        RoomJson.format
      ) {
        case ("label", value)        => JsString(value)
        case ("abbreviation", value) => JsString(value)
      },
      roomService.create
    )
  }

  def studyPrograms() = textParsingAction.async { r =>
    for {
      graduations <- graduationService.all(false)
      tus <- teachingUnitService.all(false)
      res <- createMany(
        parseCSVWithCustomKeys(r.body, StudyProgramJson.format) {
          case ("label", value)        => "label" -> JsString(value)
          case ("abbreviation", value) => "abbreviation" -> JsString(value)
          case ("graduation", value) =>
            "graduation" -> toJsonID(graduations.find(_.label == value))
          case ("teaching_unit_FK", value) =>
            "teachingUnit" -> toJsonID(
              tus.find(_.abbreviation == value)
            ) // rename ING_FK to ING
        },
        studyProgramService.create
      )
    } yield res
  }

  def modules() = textParsingAction.async { r =>
    for {
      examRegulations <- examinationRegulationService.all(false)
      users <- userService.allLecturer()
    } yield {
      toResult(parseCSVWithCustomKeys(r.body, ModuleJson.format) {
        case ("label", value)        => "label" -> JsString(value)
        case ("abbreviation", value) => "abbreviation" -> JsString(value)
        case ("examination_regulation", _) => // camel case
          "examinationRegulation" -> toJsonID(
            examRegulations.find(_.label == "???")
          )
        case ("course_manager", _) => // camel case
          "courseManager" -> toJsonID(
            users.find(_.firstname == "???")
          )
        case ("description_file_url", _) =>
          "descriptionUrl" -> JsString("???") // rename
        case ("ects", value) =>
          val ects = value match { // apply changes to origin file
            case "Unknown" => -1
            case comma if comma.contains(",") =>
              value.replace(",", ".").toDouble
            case cp if cp.contains("CP") => cp.replace("CP", "").toDouble
            case number                  => number.toDouble
          }
          "credits" -> JsNumber(ects) // rename
      })
    }
  }

  private def textParsingAction = Action(
    parse.byteString.map(
      _.utf8String
    ) // this will fix the encoding issue encoding issue
  )

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
    toProtocol(array, reads).map(seq => (array, seq))
  }
}
