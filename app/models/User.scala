package models

import database.tables.UserDbEntry

import java.util.UUID

sealed trait User extends UniqueEntity {
  val username: String
  val firstname: String
  val lastname: String
  val status: String
  val email: String
  val id: UUID
}

object User {
  def apply(
      username: String,
      firstname: String,
      lastname: String,
      status: String,
      email: String,
      title: Option[String],
      initials: Option[String],
      id: UUID
  ): User = status match {
    case StudentStatus =>
      Student(username, firstname, lastname, email, id)
    case LecturerStatus =>
      Lecturer(
        username,
        firstname,
        lastname,
        email,
        title.get,
        initials.get,
        id
      )
  }

  def apply(db: UserDbEntry): User = apply(
    db.username,
    db.firstname,
    db.lastname,
    db.status,
    db.email,
    db.title,
    db.initials,
    db.id
  )

  val StudentStatus = "student" // TODO make a type

  val LecturerStatus = "lecturer"

  case class Lecturer(
      username: String,
      firstname: String,
      lastname: String,
      email: String,
      title: String,
      initials: String,
      id: UUID
  ) extends User {
    override val status = LecturerStatus
  }

  case class Student(
      username: String,
      firstname: String,
      lastname: String,
      email: String,
      id: UUID
  ) extends User {
    override val status = StudentStatus
  }

}
