package models

import java.time.LocalDate
import java.util.UUID

import localization.LocalizedLabel
import play.api.libs.json.Json
import play.api.libs.json.Writes

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
