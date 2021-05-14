import date.{LocalDateFormat, LocalTimeFormat}
import org.joda.time.{LocalDate, LocalTime}
import play.api.libs.json.{JsString, JsSuccess}

class LocalDateTimeFormatSpec
    extends UnitSpec
    with LocalTimeFormat
    with LocalDateFormat {

  "A local Date and Time Formatter" should {
    "convert from Json to LocalTime and vice versa" in {
      val timeStr = "09:30:00"
      val localTime = LocalTime.parse(timeStr)
      val jsonTime = JsString(timeStr)

      localTimeFormat.writes(localTime) shouldBe jsonTime
      localTimeFormat.reads(jsonTime) shouldBe JsSuccess(localTime)
    }

    "convert from Json to LocalDate and vice versa" in {
      val dateStr = "1990-02-05"
      val localDate = LocalDate.parse(dateStr)
      val jsonDate = JsString(dateStr)

      localDateFormat.writes(localDate) shouldBe jsonDate
      localDateFormat.reads(jsonDate) shouldBe JsSuccess(localDate)
    }
  }
}
