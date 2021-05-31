package database.repos.filter

import database.cols.LabelColumn
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

trait LabelFilter[T <: LabelColumn] {
  self: HasDatabaseConfigProvider[JdbcProfile] =>
  import profile.api._

  private def labelFilter(label: String): T => Rep[Boolean] =
    _.hasLabel(label)

  def label: PartialFunction[(String, Seq[String]), T => Rep[Boolean]] = {
    case ("label", vs) => labelFilter(vs.head)
  }
}
