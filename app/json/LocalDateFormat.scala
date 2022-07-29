package json

import date.DateFormatPattern.dateFormatter
import json.JsonOps.FormatOps
import org.joda.time.LocalDate
import play.api.libs.json.Format

import scala.util.Try

trait LocalDateFormat {
  implicit val localDateFmt: Format[LocalDate] =
    Format
      .of[String]
      .bimapTry(
        s => Try(LocalDate.parse(s, dateFormatter)),
        _.toString(dateFormatter)
      )
}
