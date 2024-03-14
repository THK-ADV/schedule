package models

import org.joda.time.LocalDate
import play.api.libs.json.{Json, Writes}

import java.util.UUID

case class Semester(
    id: UUID,
    label: String,
    abbrev: String,
    start: LocalDate,
    end: LocalDate,
    lectureStart: LocalDate,
    lectureEnd: LocalDate
) extends UniqueEntity[UUID]

object Semester {
  implicit def writes: Writes[Semester] = Json.writes
}
