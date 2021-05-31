import database.SQLDateConverter
import org.joda.time.{LocalDate, LocalTime}

import java.sql.{Date, Time}
import scala.util.Success

class SQLDateConverterSpec extends UnitSpec with SQLDateConverter {

  "A SQLDateConverter" should {
    "convert SQLDate to LocalDate and vice versa" in {
      val dateStr = "1990-02-05"
      val sqlDate = Date.valueOf(dateStr)
      val localDate = LocalDate.parse(dateStr)

      toLocalDate(sqlDate) shouldBe localDate
      toSQLDate(localDate) shouldBe sqlDate
    }

    "convert SQLTime to LocalTime and vice versa" in {
      val timeStr = "09:30:00"
      val sqlTime = Time.valueOf(timeStr)
      val localTime = LocalTime.parse(timeStr)

      toLocalTime(sqlTime) shouldBe localTime
      toSQLTime(localTime) shouldBe sqlTime
    }

    "convert String to SQLDate" in {
      val dateStr = "2021-01-01"
      toSQLDate(dateStr) shouldBe Success(Date.valueOf(dateStr))
    }

    "convert String to SQLTime" in {
      val timeStr = "13:37:00"
      toSQLTime(timeStr) shouldBe Success(Time.valueOf(timeStr))
    }
  }
}
