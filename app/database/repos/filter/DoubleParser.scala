//package database.repos.filter
//
//import play.api.db.slick.HasDatabaseConfigProvider
//import slick.jdbc.JdbcProfile
//
//trait DoubleParser {
//  self: HasDatabaseConfigProvider[JdbcProfile] =>
//
//  import profile.api._
//
//  final def parseDouble(
//      s: Seq[String],
//      p: Double => Rep[Boolean]
//  ): Rep[Boolean] =
//    s.head.toDoubleOption.map(p) getOrElse false
//}
