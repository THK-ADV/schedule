package models

import play.api.libs.json.{Json, Writes}

import java.time.LocalDate

case class LegalHoliday(
    label: String,
    date: LocalDate,
    year: Int
)

object LegalHoliday {
  implicit def writes: Writes[LegalHoliday] = Json.writes
}
