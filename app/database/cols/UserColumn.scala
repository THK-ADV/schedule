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

  def hasUser(id: UUID): Rep[Boolean] =
    user === id

  def hasFirstname(name: String) =
    userFk.filter(_.firstname === name).exists

  def hasLastname(name: String) =
    userFk.filter(_.lastname === name).exists
}
