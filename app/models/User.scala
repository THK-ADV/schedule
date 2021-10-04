package models

import database.tables.UserDbEntry
import play.api.libs.json.{JsError, JsString, Json, OFormat}

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
        case l: Lecturer =>
          formatLecturer.writes(l) + ("status" -> JsString(LecturerStatus))
        case s: Student =>
          formatStudent.writes(s) + ("status" -> JsString(StudentStatus))
      }
  )

  implicit val formatLecturer: OFormat[Lecturer] = Json.format[Lecturer]

  implicit val formatStudent: OFormat[Student] = Json.format[Student]

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
