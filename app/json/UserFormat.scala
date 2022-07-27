package json

import models.User.{Lecturer, LecturerStatus, Student, StudentStatus}
import models.{User, UserJson}
import play.api.libs.json.{JsError, JsString, Json, OFormat}

trait UserFormat {
  implicit val userJsonFmt: OFormat[UserJson] = Json.format[UserJson]

  implicit val userFmt: OFormat[User] = OFormat.apply(
    js =>
      js.\("status").validate[String].flatMap {
        case StudentStatus  => studentFmt.reads(js)
        case LecturerStatus => lecturerFmt.reads(js)
        case other =>
          JsError(
            s"expected status to be either student or lecturer, but was $other"
          )
      },
    (user: User) =>
      user match {
        case l: Lecturer =>
          lecturerFmt.writes(l) + ("status" -> JsString(LecturerStatus))
        case s: Student =>
          studentFmt.writes(s) + ("status" -> JsString(StudentStatus))
      }
  )

  implicit val lecturerFmt: OFormat[Lecturer] = Json.format[Lecturer]

  implicit val studentFmt: OFormat[Student] = Json.format[Student]
}
