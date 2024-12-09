import java.time.LocalDateTime

import scala.util.Success
import scala.util.Try

import ops.DateOps
import play.api.mvc.AnyContent
import play.api.mvc.Request

package object controllers {
  def preferredLanguage(implicit request: Request[AnyContent]): PreferredLanguage =
    request.acceptLanguages
      .map(_.code)
      .headOption
      .map(a => PreferredLanguage(a.split('-').head))
      .getOrElse(PreferredLanguage.Default)

  def parseDate(key: String)(implicit r: Request[AnyContent]): Option[Try[LocalDateTime]] =
    r.getQueryString(key).map(a => Try(DateOps.parseDateTime(a)))

  def isExtended(implicit request: Request[AnyContent]): Boolean =
    request
      .getQueryString("extend")
      .flatMap(_.toBooleanOption)
      .getOrElse(false)

  def parseFromToDate(implicit r: Request[AnyContent]): Either[String, (LocalDateTime, LocalDateTime)] =
    (parseDate("from"), parseDate("to")) match {
      case (Some(Success(from)), Some(Success(to))) =>
        Either.cond(
          from.isBefore(to),
          (from, to),
          s"invalid date range: ${DateOps.print(from)} < ${DateOps.print(to)}"
        )
      case (Some(Success(from)), None) =>
        Right((from, from.plusMonths(1)))
      case (None, Some(Success(to))) =>
        Right((to.minusMonths(1), to))
      case (None, None) =>
        val now = LocalDateTime.now()
        Right((now.minusMonths(1), now.plusMonths(1)))
      case (_, _) =>
        Left("could not parse 'from' or 'to' parameter")
    }
}
