package database.tables

import database.cols.StringUniqueColumn
import models.Person
import models.Person.{DefaultKind, GroupKind, UnknownKind}
import slick.jdbc.PostgresProfile.api._

final class PersonTable(tag: Tag)
    extends Table[Person](tag, "people")
    with StringUniqueColumn {

  def username = column[String]("username")

  def firstname = column[String]("firstname")

  def lastname = column[String]("lastname")

  def active = column[Boolean]("active")

  def kind = column[String]("kind")

  def abbrev = column[String]("abbrev")

  def title = column[Option[String]]("title")

  def * = (
    id,
    username,
    firstname,
    lastname,
    active,
    kind,
    abbrev,
    title
  ) <> (apply, unapply)

  def apply(
      args: (
          String,
          String,
          String,
          String,
          Boolean,
          String,
          String,
          Option[String]
      )
  ): Person = apply(
    args._1,
    args._2,
    args._3,
    args._4,
    args._5,
    args._6,
    args._7,
    args._8
  )

  def apply(
      id: String,
      username: String,
      firstname: String,
      lastname: String,
      active: Boolean,
      kind: String,
      abbrev: String,
      title: Option[String]
  ): Person = kind match {
    case DefaultKind =>
      Person.Default(id, lastname, firstname, title, abbrev, username, active)
    case GroupKind =>
      Person.Group(id, username)
    case UnknownKind =>
      Person.Unknown(id, username)
  }

  def unapply(p: Person): Option[
    (String, String, String, String, Boolean, String, String, Option[String])
  ] = p match {
    case Person.Default(
          id,
          lastname,
          firstname,
          title,
          abbreviation,
          campusId,
          active
        ) =>
      Some(
        id,
        campusId,
        firstname,
        lastname,
        active,
        DefaultKind,
        abbreviation,
        title
      )
    case Person.Group(id, label) =>
      Some(id, label, "", "", true, GroupKind, "", None)
    case Person.Unknown(id, label) =>
      Some(id, label, "", "", true, UnknownKind, "", None)
  }
}
