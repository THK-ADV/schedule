package database.tables

import database.UniqueDbEntry
import database.cols.UniqueEntityColumn
import models.UserStatus
import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp
import java.util.UUID

case class UserDbEntry(
    username: String,
    firstname: String,
    lastname: String,
    status: UserStatus,
    email: String,
    title: Option[String],
    initials: Option[String],
    lastModified: Timestamp,
    id: UUID
) extends UniqueDbEntry

class UserTable(tag: Tag)
    extends Table[UserDbEntry](tag, "people")
    with UniqueEntityColumn {

  def username = column[String]("username")

  def firstname = column[String]("firstname")

  def lastname = column[String]("lastname")

  def status = column[String]("status")

  def email = column[String]("email")

  def title = column[Option[String]]("title")

  def initials = column[Option[String]]("initials")

  def hasUsername(name: String) =
    username.toLowerCase === name.toLowerCase

  def hasFirstname(name: String) =
    firstname.toLowerCase === name.toLowerCase

  def hasLastname(name: String) =
    lastname.toLowerCase === name.toLowerCase

  def hasStatus(status: String) =
    this.status.toLowerCase === status.toLowerCase

  def * = (
    username,
    firstname,
    lastname,
    status,
    email,
    title,
    initials,
    lastModified,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: (
      (
          String,
          String,
          String,
          String,
          String,
          Option[String],
          Option[String],
          Timestamp,
          UUID
      )
  ) => UserDbEntry = {
    case (
          username,
          firstname,
          lastname,
          status,
          email,
          title,
          initials,
          lastModified,
          id
        ) =>
      UserDbEntry(
        username,
        firstname,
        lastname,
        UserStatus(status).get,
        email,
        title,
        initials,
        lastModified,
        id
      )
  }

  def unmapRow: UserDbEntry => Option[
    (
        String,
        String,
        String,
        String,
        String,
        Option[String],
        Option[String],
        Timestamp,
        UUID
    )
  ] =
    a =>
      Option(
        (
          a.username,
          a.firstname,
          a.lastname,
          a.status.toString,
          a.email,
          a.title,
          a.initials,
          a.lastModified,
          a.id
        )
      )
}
