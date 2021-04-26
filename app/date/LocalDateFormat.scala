package date

import date.DateFormatPattern.dateFormatter
import org.joda.time.LocalDate
import play.api.libs.json.{Format, JsResult, JsString, JsValue}

trait LocalDateFormat {

  implicit val localDateFormat: Format[LocalDate] = new Format[LocalDate] {
    override def reads(json: JsValue): JsResult[LocalDate] =
      json.validate[String].map(LocalDate.parse(_, dateFormatter))

    override def writes(o: LocalDate): JsValue =
      JsString(o.toString(dateFormatter))
  }
}
