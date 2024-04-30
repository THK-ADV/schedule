package models

import localization.LocalizedLabel
import org.joda.time.LocalDate
import play.api.libs.json.{Json, Writes}

import java.util.UUID

case class Semester(
    id: UUID,
    deLabel: String,
    enLabel: String,
    abbrev: String,
    start: LocalDate,
    end: LocalDate,
    lectureStart: LocalDate,
    lectureEnd: LocalDate
) extends UniqueEntity[UUID]
    with LocalizedLabel

object Semester {
  implicit def writes: Writes[Semester] = Json.writes
}
