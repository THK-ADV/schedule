//package database.repos.filter
//
//import play.api.db.slick.HasDatabaseConfigProvider
//import slick.jdbc.JdbcProfile
//
//trait IntParser {
//  self: HasDatabaseConfigProvider[JdbcProfile] =>
//
//  import profile.api._
//
//  final def parseInt(
//      s: Seq[String],
//      p: Int => Rep[Boolean]
//  ): Rep[Boolean] =
//    s.head.toIntOption.map(p) getOrElse false
//}
