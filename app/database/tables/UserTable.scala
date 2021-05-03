package database.tables

import database.cols.IDColumn
import models.User
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

class UserTable(tag: Tag) extends Table[User](tag, "people") with IDColumn {

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
    id
  ) <> (mapRow, unmapRow)

  def mapRow: (
      (String, String, String, String, Option[String], Option[String], UUID)
  ) => User = {
    case (firstname, lastname, status, email, title, initials, id) =>
      User(firstname, lastname, status, email, title, initials, id)
  }

  def unmapRow: User => Option[
    (String, String, String, String, Option[String], Option[String], UUID)
  ] =
    a =>
      Option(
        (
          a.firstname,
          a.lastname,
          a.status,
          a.email,
          a.optTitle,
          a.optInitials,
          a.id
        )
      )
}
