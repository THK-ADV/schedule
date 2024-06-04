package database.view

import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.sql.SqlAction

trait MaterializedView { self: HasDatabaseConfigProvider[JdbcProfile] =>
  import profile.api._

  protected def name: String

  protected def refreshView(): SqlAction[Int, NoStream, Effect] =
    sqlu"refresh materialized view #$name"
}
