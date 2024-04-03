package database

import models.ModulePart
import ops.DateOps
import org.joda.time.{LocalDate, LocalTime}
import slick.jdbc.PostgresProfile.api._

import java.sql.{Date, Time}

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

  implicit val modulePartColumnType: BaseColumnType[ModulePart] =
    MappedColumnType.base[ModulePart, String](
      _.id,
      ModulePart.apply
    )

  implicit val modulePartsColumnType: BaseColumnType[List[ModulePart]] =
    MappedColumnType.base[List[ModulePart], String](
      xs => if (xs.isEmpty) "" else xs.mkString(","),
      x => if (x.isEmpty) Nil else x.split(",").toList.map(ModulePart.apply)
    )

  implicit val intListColumnType: BaseColumnType[List[Int]] =
    MappedColumnType.base[List[Int], String](
      xs => if (xs.isEmpty) "" else xs.mkString(","),
      x => if (x.isEmpty) Nil else x.split(",").toList.map(_.toInt)
    )
}
