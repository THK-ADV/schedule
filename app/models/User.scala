package models

import play.api.libs.json.{JsError, Json, OFormat}

import java.util.UUID

abstract class User(
    val firstname: String,
    val lastname: String,
    val status: String,
    val email: String,
    val optTitle: Option[String],
    val optInitials: Option[String],
    val id: UUID
) extends UniqueEntity

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
      override val firstname: String,
      override val lastname: String,
      override val email: String,
      title: String,
      initials: String,
      override val id: UUID
  ) extends User(
        firstname,
        lastname,
        LecturerStatus,
        email,
        Some(title),
        Some(initials),
        id
      )

  case class Student(
      override val firstname: String,
      override val lastname: String,
      override val email: String,
      override val id: UUID
  ) extends User(firstname, lastname, StudentStatus, email, None, None, id)

}
