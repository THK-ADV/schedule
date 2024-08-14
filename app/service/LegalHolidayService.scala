package service

import database.repos.LegalHolidayRepository
import models.LegalHoliday
import ops.DateOps
import play.api.libs.json._
import play.api.libs.ws.WSClient

import java.time.{LocalDate, LocalDateTime}
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext
import scala.util.control.NonFatal

@Singleton
final class LegalHolidayService @Inject() (
    val repo: LegalHolidayRepository,
    val ws: WSClient,
    implicit val ctx: ExecutionContext
) {

  implicit def reads: Reads[LocalDate] =
    Reads
      .of[String]
      .flatMapResult(s =>
        try JsSuccess(DateOps.parseDate(s))
        catch {
          case NonFatal(e) => JsError(e.getMessage)
        }
      )

  def all(from: LocalDateTime, to: LocalDateTime) =
    repo.all(from.toLocalDate, to.toLocalDate)

  def recreate(year: Int) = {
    def parse(js: JsValue) =
      js.\("feiertage")
        .validate[JsArray]
        .map(holidays =>
          holidays.value
            .map { day =>
              for {
                date <- day.\("date").validate[LocalDate]
                label <- day.\("fname").validate[String]
              } yield LegalHoliday(
                label,
                date,
                year
              )
            }
            .collect { case JsSuccess(a, _) => a }
        )
        .getOrElse(Seq.empty)
        .toList

    for {
      holidays <- ws
        .url(s"https://get.api-feiertage.de?years=$year&states=nw")
        .get()
        .map(resp => parse(resp.json))
      _ <- repo.delete(year)
      res <- repo.createMany(holidays)
    } yield res
  }
}
