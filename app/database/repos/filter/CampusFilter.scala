//package database.repos.filter
//
//import database.cols.CampusColumn
//import play.api.db.slick.HasDatabaseConfigProvider
//import slick.jdbc.JdbcProfile
//
//trait CampusFilter[T <: CampusColumn] extends UUIDParser {
//  self: HasDatabaseConfigProvider[JdbcProfile] with UUIDParser =>
//  import profile.api._
//
//  def campus: PartialFunction[(String, Seq[String]), T => Rep[Boolean]] = {
//    case ("campus", vs)              => t => parseUUID(vs, t.campus)
//    case ("campus_label", vs)        => _.label(vs.head)
//    case ("campus_abbreviation", vs) => _.abbrev(vs.head)
//  }
//}
