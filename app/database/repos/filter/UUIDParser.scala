//package database.repos.filter
//
//import play.api.db.slick.HasDatabaseConfigProvider
//import slick.jdbc.JdbcProfile
//
//import java.util.UUID
//
//trait UUIDParser {
//  self: HasDatabaseConfigProvider[JdbcProfile] =>
//
//  import profile.api._
//
//  final def parseUUID(s: Seq[String], p: UUID => Rep[Boolean]): Rep[Boolean] =
//    Option(UUID.fromString(s.head)).map(p) getOrElse false
//}
