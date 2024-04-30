package database.view

import controllers.PreferredLanguage
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.{GetResult, JdbcProfile}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

trait JsonView { self: HasDatabaseConfigProvider[JdbcProfile] =>
  import profile.api._

  protected def name: String

  implicit def jsonResult: GetResult[String] =
    GetResult(_.nextString())

  def getAllFromView(implicit ctx: ExecutionContext): Future[String] =
    db.run(sql"select * from #$name".as).map(_.head)

  def getAllFromView(
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
