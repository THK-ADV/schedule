//package database.repos.filter
//
//import play.api.db.slick.HasDatabaseConfigProvider
//import slick.jdbc.JdbcProfile
//
//trait BooleanParser {
//  self: HasDatabaseConfigProvider[JdbcProfile] =>
//
//  import profile.api._
//
//  final def parseBoolean(
//      s: Seq[String],
//      p: Boolean => Rep[Boolean]
//  ): Rep[Boolean] =
//    s.head.toBooleanOption.map(p) getOrElse false
//}
