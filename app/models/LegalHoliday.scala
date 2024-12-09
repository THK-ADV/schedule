package models

import java.time.LocalDate

import play.api.libs.json.Json
import play.api.libs.json.Writes

case class LegalHoliday(
    label: String,
    date: LocalDate,
    year: Int
)

object LegalHoliday {
  implicit def writes: Writes[LegalHoliday] = Json.writes
}
