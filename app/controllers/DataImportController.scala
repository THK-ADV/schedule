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

import java.sql.Timestamp
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
    val facultyRepository: FacultyRepository,
    val campusRepository: CampusRepository,
    val campusService: CampusService,
    val moduleRepository: ModuleRepository,
    val moduleExaminationRegulationRepository: ModuleExaminationRegulationRepository,
    val subModuleService: SubModuleService
) extends AbstractController(cc)
    with LocalDateFormat {

  case class TmpUser(
      username: String,
      firstname: String,
      lastname: String,
      email: String
  )

  val datePattern = DateTimeFormat.forPattern("yy-MM-dd")

  def primitives() = Action.async { r =>
    val user = UserDbEntry(
      "???",
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

    val gm = CampusDbEntry("Gummersbach", "GM", now, UUID.randomUUID())

    for {
      gs <- Future.sequence(graduations.map(a => graduationRepo.create(a, Nil)))
      u <- userRepo.create(user, Nil)
      c <- campusRepository.create(gm, Nil)
    } yield Ok(
      Json.obj(
        "user" -> u,
        "graduations" -> gs,
        "camus" -> c
      )
    )
  }

  def lecturer() = Action(parse.json).async { r =>
    implicit val reads: Reads[TmpUser] = Json.reads[TmpUser]

    val lecs = r.body
      .validate[List[TmpUser]]
      .get
      .map(l =>
        UserDbEntry(
          l.username,
          l.firstname,
          l.lastname,
          User.LecturerStatus,
          l.email,
          Some("???"),
          Some("?.?"),
          now,
          UUID.randomUUID()
        )
      )

    Future
      .sequence(lecs.map(l => userRepo.create(l, Nil)))
      .map(xs => Ok(Json.toJson(xs)))
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
    for {
      facs <- facultyService.all(false)
      res <- createMany(
        parseCSV(r.body, TeachingUnitJson.format) {
          case ("number", value) =>
            JsNumber(value.toInt)
          case ("label", value) =>
            JsString(value)
          case ("abbreviation", value) =>
            JsString(value)
          case ("faculty", value) =>
            toJsonID(facs.find(_.number == value.toInt))
        },
        teachingUnitService.create
      )
    } yield res
  }

  def semesters() = textParsingAction.async { r =>
    createMany(
      parseCSV(r.body, SemesterJson.format) {
        case ("label", value)        => JsString(value)
        case ("abbreviation", value) => JsString(value)
        case (_, value)              => parseDate(value)
      },
      semesterService.create
    )
  }

  def rooms() = textParsingAction.async { r =>
    for {
      campus <- campusService.all(false)
      res <- createMany(
        parseCSV(
          r.body,
          RoomJson.format
        ) {
          case ("label", value) =>
            JsString(value)
          case ("abbreviation", value) =>
            JsString(value)
          case ("campus", value) =>
            toJsonID(
              campus.find(_.abbreviation.toLowerCase == value.toLowerCase)
            )
        },
        roomService.create
      )
    } yield res
  }

  def studyPrograms() = textParsingAction.async { r =>
    for {
      graduations <- graduationService.all(false)
      tus <- teachingUnitService.all(false)
      res <- createMany(
        parseCSV(r.body, StudyProgramJson.format) {
          case ("label", value) =>
            JsString(value)
          case ("abbreviation", value) =>
            JsString(value)
          case ("graduation", value) =>
            toJsonID(graduations.find(_.label == value))
          case ("teachingUnit", value) =>
            toJsonID(tus.find(_.abbreviation == value))
        },
        studyProgramService.create
      )
    } yield res
  }

  def examinationRegulations() = textParsingAction.async { r =>
    for {
      studyPrograms <- studyProgramService.all(false)
      graduations <- graduationService.all(false)
      res <- createMany(
        parseCSVWithCustomKeys(r.body, ExaminationRegulationJson.format) {
          case ("number", value) =>
            "number" -> JsNumber(value.toInt)
          case ("activation_date", value) =>
            "start" -> parseDate(value)
          case ("expiring_date", value) =>
            "end" -> (if (value == "null") JsNull else parseDate(value))
          case ("studyProgram", value) =>
            val Array(sp, g, _) = value.split("_")
            val grad = graduations.find(_.abbreviation.take(1) == g).get.id
            "studyProgram" -> toJsonID(
              studyPrograms.find(s =>
                s.abbreviation == sp && s.graduationId == grad
              )
            )
        },
        examinationRegulationService.create
      )
    } yield res
  }

  def modules = textParsingAction.async { r =>
    def toModuleDb(json: ModuleJson) = ModuleDbEntry(
      json.courseManager,
      json.label,
      json.abbreviation,
      json.credits,
      json.descriptionUrl,
      now,
      UUID.randomUUID
    )

    def toAssocDb(json: ModuleExaminationRegulationJson) =
      ModuleExaminationRegulationDbEntry(
        json.module,
        json.examinationRegulation,
        json.mandatory,
        now,
        UUID.randomUUID
      )

    val all = r.body.linesIterator.toVector
    val header = all.head.split(";")
    val rows = all.drop(1)

    for {
      users <- userService.allLecturer()
      exams <- examinationRegulationService.allAtoms(Map.empty)
      unknown = users.find(_.username == "???").get
      dbEntries = rows
        .map { row =>
          val cols = row.split(";")
          header.zip(cols).foldLeft((Json.obj(), List.empty[JsObject])) {
            case (json, ("id", _)) =>
              json
            case (json, ("po", "Unknown")) =>
              json
            case ((module, assocs), (key, value)) =>
              key match {
                case "label" =>
                  (module + (key -> JsString(value)), assocs)
                case "abbreviation" =>
                  (module + (key -> JsString(value)), assocs)
                case "courseManager" =>
                  val user =
                    if (value == "UnknownFk") unknown
                    else
                      users.find(
                        _.username.toLowerCase == value.toLowerCase
                      ) getOrElse unknown
                  (module + (key -> toJsonID(Some(user))), assocs)
                case "descriptionUrl" =>
                  (module + (key -> JsString("???")), assocs)
                case "credits" =>
                  (module + (key -> parseCredits(value)), assocs)
                case "po" =>
                  val xs = value
                    .split(",")
                    .map { po =>
                      val Array(sp, g, pon) = po.split("_")
                      val exam = exams
                        .find(e =>
                          e.studyProgram.abbreviation == sp &&
                            e.studyProgram.graduation.abbreviation.head == g.head &&
                            e.number == pon.toInt
                        )
                        .get
                      Json.obj(
                        "module" -> "???",
                        "examinationRegulation" -> exam.id,
                        "mandatory" -> false
                      )
                    }
                    .toList
                  (module, assocs ::: xs)
              }
          }
        }
        .filter(_._2.nonEmpty)
        .map { case (moduleJson, assocs) =>
          val module = moduleJson.validate[ModuleJson].map(toModuleDb).get
          (
            module,
            assocs
              .map(a =>
                a.copy(a.value.map { case (k, v) =>
                  val newValue =
                    if (k == "module") JsString(module.id.toString) else v
                  k -> newValue
                })
              )
              .map(
                _.validate[ModuleExaminationRegulationJson]
                  .map(toAssocDb)
                  .get
              )
          )
        }
      modules <- Future.sequence(
        dbEntries.map(e => moduleRepository.create(e._1, Nil))
      )
      assocs <- Future.sequence(
        dbEntries
          .flatMap(_._2)
          .map(moduleExaminationRegulationRepository.create(_, Nil))
      )
    } yield Ok(
      Json.obj(
        "modules" -> modules,
        "assocs" -> assocs
      )
    )
  }

  def subModules() = textParsingAction.async { r =>
    for {
      modules <- moduleService.all(false)
      magicID = UUID.randomUUID()
      subModuleJson = parseCSV2(r.body, SubModuleJson.format) {
        case ("label", value)               => List(JsString(value))
        case ("abbreviation", value)        => List(JsString(value))
        case ("recommendedSemester", value) => List(JsNumber(value.toInt))
        case ("credits", value)             => List(parseCredits(value))
        case ("descriptionUrl", _)          => List(JsString("???"))
        case ("language", value)            => List(JsString(Language(value).toString))
        case ("season", value)              => List(JsString(Season(value).toString))
        case ("module", value) =>
          List(
            JsString(
              modules
                .find(_.abbreviation == value)
                .map(_.id)
                .getOrElse(magicID)
                .toString
            )
          )
        case _ => Nil
      }.get._2
        .filterNot(_.module == magicID)
      subModules <- Future.sequence(
        subModuleJson.map(subModuleService.create)
      )
    } yield Ok(Json.toJson(subModules))
  }

  private def parseCredits(value: String): JsValue = {
    val ects = value match { // apply changes to origin file
      case "Unknown" => -1
      case comma if comma.contains(",") =>
        value.replace(",", ".").toDouble
      case cp if cp.contains("CP") => cp.replace("CP", "").toDouble
      case number                  => number.toDouble
    }
    JsNumber(ects)
  }

  private def parseDate(str: String): JsValue =
    localDateFormat.writes(LocalDate.parse(str, datePattern))

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
    parseCSV2(body, reads)((k, v) => List(f(k, v)))

  private def parseCSV2[A](body: String, reads: Reads[A])(
      f: (String, String) => List[JsValue]
  ): JsResult[(JsArray, Seq[A])] =
    parseCSVWithCustomKeys2(body, reads)((k, v) => f(k, v).map(k -> _))

  private def parseCSVWithCustomKeys[A](body: String, reads: Reads[A])(
      f: (String, String) => (String, JsValue)
  ): JsResult[(JsArray, Seq[A])] =
    parseCSVWithCustomKeys2(body, reads)((k, v) => List(f(k, v)))

  private def parseCSVWithCustomKeys2[A](body: String, reads: Reads[A])(
      f: (String, String) => List[(String, JsValue)]
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
          f(key, value).foldLeft(json) { case (acc, j) =>
            acc + j
          }
      }
    }

    val array = JsArray(res)
    toProtocol(array, reads).map(seq => (array, seq))
  }
}
