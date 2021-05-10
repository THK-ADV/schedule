package models

import database.tables.UserDbEntry
import play.api.libs.json.{JsError, Json, OFormat}

import java.util.UUID

sealed trait User extends UniqueEntity {
  val firstname: String
  val lastname: String
  val status: String
  val email: String
  val id: UUID
}

object User {
  def apply(
      firstname: String,
      lastname: String,
      status: String,
      email: String,
      title: Option[String],
      initials: Option[String],
      id: UUID
  ): User = status match {
    case StudentStatus =>
      Student(firstname, lastname, email, id)
    case LecturerStatus =>
      Lecturer(firstname, lastname, email, title.get, initials.get, id)
  }

  def apply(db: UserDbEntry): User = apply(
    db.firstname,
    db.lastname,
    db.status,
    db.email,
    db.title,
    db.initials,
    db.id
  )

  val StudentStatus = "student"

  val LecturerStatus = "lecturer"

  implicit val formatUser: OFormat[User] = OFormat.apply(
    js =>
      js.\("status").validate[String].flatMap {
        case StudentStatus  => formatStudent.reads(js)
        case LecturerStatus => formatLecturer.reads(js)
        case other =>
          JsError(
            s"expected status to be either student or lecturer, but was $other"
          )
      },
    (user: User) =>
      user match {
        case l: Lecturer => formatLecturer.writes(l)
        case s: Student  => formatStudent.writes(s)
      }
  )

  implicit val formatLecturer: OFormat[Lecturer] = Json.format[Lecturer]

  implicit val formatStudent: OFormat[Student] = Json.format[Student]

  case class Lecturer(
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
      firstname: String,
      lastname: String,
      email: String,
      id: UUID
  ) extends User {
    override val status = StudentStatus
  }

}
