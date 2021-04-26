package database.cols

import slick.jdbc.PostgresProfile.api._

import java.util.UUID

trait IDColumn {
  self: Table[_] =>
  def id = column[UUID]("id", O.PrimaryKey)

  def hasID(id: UUID): Rep[Boolean] =
    this.id === id
}
