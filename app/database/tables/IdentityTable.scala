package database.tables

import database.StringUniqueColumn
import models.Identity
import slick.jdbc.PostgresProfile.api._

final class IdentityTable(tag: Tag)
    extends Table[Identity](tag, "identity")
    with StringUniqueColumn {

  def campusId = column[String]("campus_id")

  def firstname = column[String]("firstname")

  def lastname = column[String]("lastname")

  def kind = column[String]("kind")

  def abbrev = column[String]("abbrev")

  def title = column[String]("title")

  def * = (
    id,
    campusId,
    firstname,
    lastname,
    kind,
    abbrev,
    title
  ) <> (apply, Identity.unapply)

  def apply(
      args: (
          String,
          String,
          String,
          String,
          String,
          String,
          String
      )
  ): Identity = Identity.apply(
    args._1,
    args._2,
    args._3,
    args._4,
    args._5,
    args._6,
    args._7
  )
}
