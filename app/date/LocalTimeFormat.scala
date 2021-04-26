package date

import date.DateFormatPattern.timeFormatter
import org.joda.time.LocalTime
import play.api.libs.json.{Format, JsResult, JsString, JsValue}

trait LocalTimeFormat {

  implicit val localTimeFormat: Format[LocalTime] = new Format[LocalTime] {
    override def reads(json: JsValue): JsResult[LocalTime] =
      json.validate[String].map(LocalTime.parse(_, timeFormatter))

    override def writes(o: LocalTime): JsValue =
      JsString(o.toString(timeFormatter))
  }
}
