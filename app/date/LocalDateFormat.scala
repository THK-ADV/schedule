package date

import date.DateFormatPattern.dateFormatter
import org.joda.time.LocalDate
import play.api.libs.json.Format

import scala.util.Try

trait LocalDateFormat {

  import controllers.JsonOps.FormatOps

  implicit val localDateFormat: Format[LocalDate] =
    Format
      .of[String]
      .bimapTry(
        s => Try(LocalDate.parse(s, dateFormatter)),
        _.toString(dateFormatter)
      )
}
