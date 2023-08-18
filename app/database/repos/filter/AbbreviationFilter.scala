//package database.repos.filter
//
//import database.cols.AbbreviationColumn
//import play.api.db.slick.HasDatabaseConfigProvider
//import slick.jdbc.JdbcProfile
//
//trait AbbreviationFilter[T <: AbbreviationColumn] {
//  self: HasDatabaseConfigProvider[JdbcProfile] =>
//  import profile.api._
//
//  def abbrev: PartialFunction[(String, Seq[String]), T => Rep[Boolean]] = {
//    case ("abbreviation", vs) => t => t.hasAbbreviation(vs.head)
//  }
//}
