package models

import org.joda.time.{LocalDate, LocalTime}

import java.util.UUID

case class Reservation(
    id: UUID,
    room: UUID,
    date: LocalDate,
    start: LocalTime,
    end: LocalTime,
    source: String,
    description: String
) extends UniqueEntity[UUID]
