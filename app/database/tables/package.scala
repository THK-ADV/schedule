package database

import models.CourseId
import ops.DateOps
import org.joda.time.{LocalDate, LocalDateTime, LocalTime}
import slick.jdbc.PostgresProfile.api._

import java.sql.{Date, Time, Timestamp}

package object tables {
  implicit val localDateColumnType: BaseColumnType[LocalDate] =
    MappedColumnType.base[LocalDate, Date](
      localDate => Date.valueOf(DateOps.print(localDate)),
      date => new LocalDate(date.getTime)
    )

  implicit val localTimeColumnType: BaseColumnType[LocalTime] =
    MappedColumnType.base[LocalTime, Time](
      localTime => Time.valueOf(DateOps.print(localTime)),
      time => new LocalTime(time.getTime)
    )

  implicit val localDateTimeColumnType: BaseColumnType[LocalDateTime] =
    MappedColumnType.base[LocalDateTime, Timestamp](
      localDateTime => Timestamp.valueOf(DateOps.print(localDateTime)),
      timestamp => new LocalDateTime(timestamp.getTime)
    )

  implicit val modulePartColumnType: BaseColumnType[CourseId] =
    MappedColumnType.base[CourseId, String](
      _.id,
      CourseId.apply
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
