package database.repos

import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

import java.util.UUID

trait FilterValueParser {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  final def parseUUID(
      s: Seq[String],
      p: UUID => Rep[Boolean]
  ): Rep[Boolean] =
    Option(UUID.fromString(s.head)).map(p) getOrElse false

  final def parseInt(
      s: Seq[String],
      p: Int => Rep[Boolean]
  ): Rep[Boolean] =
    s.head.toIntOption.map(p) getOrElse false

  final def parseBoolean(
      s: Seq[String],
      p: Boolean => Rep[Boolean]
  ): Rep[Boolean] =
    s.head.toBooleanOption.map(p) getOrElse false
}
