package database.view

import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.{GetResult, JdbcProfile}

import scala.concurrent.{ExecutionContext, Future}

trait JsonView { self: HasDatabaseConfigProvider[JdbcProfile] =>
  import profile.api._

  protected def name: String

  implicit def jsonResult: GetResult[String] =
    GetResult(_.nextString())

  def getAllFromView(implicit ctx: ExecutionContext): Future[String] =
    db.run(sql"select * from #$name".as).map(_.head)
}
