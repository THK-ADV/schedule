package database.cols

import database.tables.UserTable
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

trait UserColumn {
  self: Table[_] =>
  protected def userColumnName: String

  def user = column[UUID](userColumnName)

  def userFk =
    foreignKey("user", user, TableQuery[UserTable])(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )

  def user(id: UUID): Rep[Boolean] =
    user === id

  def hasUsername(name: String) =
    userFk.filter(_.hasUsername(name)).exists

  def hasFirstname(name: String) =
    userFk.filter(_.hasFirstname(name)).exists

  def hasLastname(name: String) =
    userFk.filter(_.hasLastname(name)).exists

  def hasStatus(status: String) =
    userFk.filter(_.hasStatus(status)).exists
}
