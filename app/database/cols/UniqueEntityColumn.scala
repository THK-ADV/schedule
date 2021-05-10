package database.cols

import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp
import java.util.UUID

trait UniqueEntityColumn {
  self: Table[_] =>
  def id = column[UUID]("id", O.PrimaryKey)

  def lastModified = column[Timestamp]("last_modified")

  def hasID(id: UUID): Rep[Boolean] =
    this.id === id

  def lastModifiedSince(timestamp: Timestamp): Rep[Boolean] =
    lastModified >= timestamp
}
