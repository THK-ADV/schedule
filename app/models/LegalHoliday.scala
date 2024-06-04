package models

import org.joda.time.LocalDate
import play.api.libs.json.{Json, Writes}

case class LegalHoliday(
    label: String,
    date: LocalDate,
    year: Int
)

object LegalHoliday {
  implicit def writes: Writes[LegalHoliday] = Json.writes
}
