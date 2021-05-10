package database.tables

import database.UniqueDbEntry
import database.cols.UniqueEntityColumn
import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp
import java.util.UUID

case class UserDbEntry(
    firstname: String,
    lastname: String,
    status: String,
    email: String,
    title: Option[String],
    initials: Option[String],
    lastModified: Timestamp,
    id: UUID
) extends UniqueDbEntry

class UserTable(tag: Tag)
    extends Table[UserDbEntry](tag, "people")
    with UniqueEntityColumn {

  def firstname = column[String]("firstname")

  def lastname = column[String]("lastname")

  def status = column[String]("status")

  def email = column[String]("email")

  def title = column[Option[String]]("title")

  def initials = column[Option[String]]("initials")

  def * = (
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
          Option[String],
          Option[String],
          Timestamp,
          UUID
      )
  ) => UserDbEntry = {
    case (
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
        firstname,
        lastname,
        status,
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
        Option[String],
        Option[String],
        Timestamp,
        UUID
    )
  ] =
    a =>
      Option(
        (
          a.firstname,
          a.lastname,
          a.status,
          a.email,
          a.title,
          a.initials,
          a.lastModified,
          a.id
        )
      )
}
