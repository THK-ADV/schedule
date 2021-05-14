package controllers

import database.repos.UserRepository
import database.tables.UserDbEntry
import date.LocalDateFormat
import models.{FacultyJson, RoomJson, SemesterJson, TeachingUnitJson}
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import play.api.libs.json.{JsResult, _}
import play.api.mvc.{AbstractController, ControllerComponents}

import java.sql.Timestamp
import java.util.UUID
import javax.inject.Inject
import scala.concurrent.ExecutionContext

class DataImportController @Inject() (
    cc: ControllerComponents,
    implicit val ctx: ExecutionContext,
    val userRepo: UserRepository
) extends AbstractController(cc)
    with LocalDateFormat {

  val dayPattern = DateTimeFormat.forPattern("dd.MM.yy")

  def bootstrap() = Action.async { r =>
    val user = UserDbEntry(
      "???",
      "???",
      "???",
      "???",
      Some("???"),
      Some("?.?"),
      now,
      UUID.randomUUID()
    )

    for {
      _ <- userRepo.create(user, _ => List.empty)
    } yield Ok(JsBoolean(true))
  }

  def faculties() = Action(parse.text) { r =>
    toResult(parseCSV(r.body, FacultyJson.format) {
      case ("number", value)       => JsNumber(value.toInt)
      case ("label", value)        => JsString(value)
      case ("abbreviation", value) => JsString(value)
    })
  }

  def teachingUnits() = Action(parse.text) { r =>
    toResult(parseCSV(r.body, TeachingUnitJson.format) {
      case ("number", value)       => JsNumber(value.toInt)
      case ("label", value)        => JsString(value)
      case ("abbreviation", value) => JsString(value)
    })
  }

  def semesters() = Action(parse.text) { r =>
    toResult(parseCSV(r.body, SemesterJson.format) {
      case ("label", value)        => JsString(value)
      case ("abbreviation", value) => JsString(value)
      case (_, value) =>
        localDateFormat.writes(LocalDate.parse(value, dayPattern))
    })
  }

  def rooms() = Action(parse.text) { r =>
    toResult(parseCSV(r.body, RoomJson.format) {
      case ("label", value)        => JsString(value)
      case ("abbreviation", value) => JsString(value)
    })
  }

  private def toResult[A](json: JsResult[(JsArray, Seq[A])]) = json match {
    case JsSuccess((json, protocols), _) =>
      println(protocols)
      Ok(json)
    case JsError(errors) =>
      BadRequest(JsString(errors.toString()))
  }

  private def now = new Timestamp(System.currentTimeMillis())

  private def toProtocol[A](array: JsArray, reads: Reads[A]): JsResult[Seq[A]] =
    Reads.list(reads).reads(array)

  private def parseCSV[A](body: String, reads: Reads[A])(
      f: (String, String) => JsValue
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
          json.+(key -> f(key, value))
      }
    }

    val array = JsArray(res)

    toProtocol(array, reads).map(seq => (array, seq))
  }
}
