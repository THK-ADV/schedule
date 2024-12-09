package database.view

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.util.control.NonFatal

import controllers.PreferredLanguage
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

trait JsonView extends JsonViewGetResult {
  self: HasDatabaseConfigProvider[JdbcProfile] =>
  import profile.api._

  protected def name: String

  protected def getAllFromView(implicit ctx: ExecutionContext): Future[String] =
    db.run(sql"select * from #$name".as).map(_.head)

  protected def getAllFromView(
      lang: PreferredLanguage
  )(implicit ctx: ExecutionContext): Future[String] = {
    def go(lang: PreferredLanguage, retries: Int): Future[String] =
      db.run(sql"select * from #${name}_#${lang.value}".as)
        .map(_.head)
        .recoverWith {
          case NonFatal(_) if retries > 0 =>
            go(PreferredLanguage.Default, retries - 1)
        }
    go(lang, 1)
  }
}
