package database.cols

import slick.jdbc.PostgresProfile.api._

trait LabelColumn {
  self: Table[_] =>
  def label = column[String]("label")

  def hasLabel(label: String): Rep[Boolean] =
    this.label.toLowerCase === label.toLowerCase

  def likeLabel(label: String): Rep[Boolean] =
    this.label.toLowerCase like s"%${label.toLowerCase}"
}
