package database

import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

import models.CourseId
import models.SemesterPlanEntryType
import slick.jdbc.PostgresProfile.api._

package object tables {
  implicit val localDateColumnType: BaseColumnType[LocalDate] =
    MappedColumnType.base[LocalDate, Date](
      Date.valueOf,
      _.toLocalDate
    )

  implicit val localTimeColumnType: BaseColumnType[LocalTime] =
    MappedColumnType.base[LocalTime, Time](
      Time.valueOf,
      _.toLocalTime
    )

  implicit val localDateTimeColumnType: BaseColumnType[LocalDateTime] =
    MappedColumnType.base[LocalDateTime, Timestamp](
      localDateTime => Timestamp.valueOf(localDateTime),
      _.toLocalDateTime
    )

  implicit val modulePartColumnType: BaseColumnType[CourseId] =
    MappedColumnType.base[CourseId, String](
      _.id,
      CourseId.apply
    )

  implicit val semesterPlanEntryTypeColumnType: BaseColumnType[SemesterPlanEntryType] =
    MappedColumnType.base[SemesterPlanEntryType, String](
      _.id,
      SemesterPlanEntryType.apply
    )

  implicit val modulePartsColumnType: BaseColumnType[List[CourseId]] =
    MappedColumnType.base[List[CourseId], String](
      xs => if (xs.isEmpty) "" else xs.mkString(","),
      x => if (x.isEmpty) Nil else x.split(",").toList.map(CourseId.apply)
    )

  implicit val intListColumnType: BaseColumnType[List[Int]] =
    MappedColumnType.base[List[Int], String](
      xs => if (xs.isEmpty) "" else xs.mkString(","),
      x => if (x.isEmpty) Nil else x.split(",").toList.map(_.toInt)
    )
}
