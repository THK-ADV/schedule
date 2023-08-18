package database

import models.{ModulePart, ModuleType}
import org.joda.time.{LocalDate, LocalTime}
import slick.jdbc.PostgresProfile.api._

import java.sql.{Date, Time}

package object tables {
  implicit val localDateColumnType: BaseColumnType[LocalDate] =
    MappedColumnType.base[LocalDate, Date](
      localDate => Date.valueOf(localDate.toString("yyyy-MM-dd")),
      date => new LocalDate(date.getTime)
    )

  implicit val localTimeColumnType: BaseColumnType[LocalTime] =
    MappedColumnType.base[LocalTime, Time](
      localTime => Time.valueOf(localTime.toString("HH:mm:ss")),
      time => new LocalTime(time.getTime)
    )

  implicit val moduleTypeColumnType: BaseColumnType[ModuleType] =
    MappedColumnType.base[ModuleType, String](
      _.value,
      ModuleType.apply
    )

  implicit val modulePartColumnType: BaseColumnType[ModulePart] =
    MappedColumnType.base[ModulePart, String](
      _.value,
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
