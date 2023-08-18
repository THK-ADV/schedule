//package database.repos.filter
//
//import database.cols.DateStartEndColumn
//import play.api.db.slick.HasDatabaseConfigProvider
//import slick.jdbc.JdbcProfile
//
//trait DateStartEndFilter[T <: DateStartEndColumn] extends DateTimeParser {
//  self: HasDatabaseConfigProvider[JdbcProfile] =>
//
//  import profile.api._
//
//  def dateStartEnd
//      : PartialFunction[(String, Seq[String]), T => Rep[Boolean]] = {
//    case ("date", vs)        => t => parseDate(vs, t.onDate)
//    case ("start", vs)       => t => parseTime(vs, t.onStart)
//    case ("end", vs)         => t => parseTime(vs, t.onEnd)
//    case ("since_start", vs) => t => parseTime(vs, t.sinceStart)
//    case ("until_end", vs)   => t => parseTime(vs, t.untilEnd)
//    case ("since_date", vs)  => t => parseDate(vs, t.sinceDate)
//    case ("until_date", vs)  => t => parseDate(vs, t.untilDate)
//  }
//}
