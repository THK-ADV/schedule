//package database.repos.filter
//
//import database.SQLDateConverter
//import play.api.db.slick.HasDatabaseConfigProvider
//import slick.jdbc.JdbcProfile
//
//import java.sql.{Date, Time}
//
//trait DateTimeParser extends SQLDateConverter {
//  self: HasDatabaseConfigProvider[JdbcProfile] =>
//
//  import profile.api._
//
//  def parseDate(str: Seq[String], f: Date => Rep[Boolean]): Rep[Boolean] =
//    toSQLDate(str.head).map(f).getOrElse(false)
//
//  def parseTime(str: Seq[String], f: Time => Rep[Boolean]): Rep[Boolean] =
//    toSQLTime(str.head).map(f).getOrElse(false)
//}
