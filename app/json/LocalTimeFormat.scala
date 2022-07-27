package json

import date.DateFormatPattern.timeFormatter
import json.JsonOps.FormatOps
import org.joda.time.LocalTime
import play.api.libs.json.Format

import scala.util.Try

trait LocalTimeFormat {
  implicit val localTimeFmt: Format[LocalTime] =
    Format
      .of[String]
      .bimapTry(
        s => Try(LocalTime.parse(s, timeFormatter)),
        _.toString(timeFormatter)
      )
}
