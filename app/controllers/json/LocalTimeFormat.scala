package controllers.json

import DateFormatPattern.timeFormatter
import org.joda.time.LocalTime
import play.api.libs.json.Format

import scala.util.Try

trait LocalTimeFormat {
  import controllers.json.JsonOps.FormatOps

  implicit val localTimeFormat: Format[LocalTime] =
    Format
      .of[String]
      .bimapTry(
        s => Try(LocalTime.parse(s, timeFormatter)),
        _.toString(timeFormatter)
      )
}
