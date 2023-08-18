//package database.repos.filter
//
//import database.cols.RoomColumn
//import play.api.db.slick.HasDatabaseConfigProvider
//import slick.jdbc.JdbcProfile
//
//trait RoomFilter[T <: RoomColumn] extends UUIDParser {
//  self: HasDatabaseConfigProvider[JdbcProfile] =>
//
//  import profile.api._
//
//  def allRooms: PartialFunction[(String, Seq[String]), T => Rep[Boolean]] =
//    room orElse campus
//
//  def room: PartialFunction[(String, Seq[String]), T => Rep[Boolean]] = {
//    case ("room", vs)              => t => parseUUID(vs, t.room)
//    case ("room_label", vs)        => _.label(vs.head)
//    case ("room_abbreviation", vs) => _.abbrev(vs.head)
//  }
//
//  def campus: PartialFunction[(String, Seq[String]), T => Rep[Boolean]] = {
//    case ("campus", vs)              => t => parseUUID(vs, t.campus)
//    case ("campus_label", vs)        => _.campusLabel(vs.head)
//    case ("campus_abbreviation", vs) => _.campusAbbrev(vs.head)
//  }
//}
