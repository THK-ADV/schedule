package database.repos

import database.repos.filter.UUIDParser
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.util.Try

trait FilterValueParser extends UUIDParser {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

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

  final def parseTry[A](
      t: Try[A],
      p: A => Rep[Boolean]
  ): Rep[Boolean] =
    t.map(p) getOrElse false
}
