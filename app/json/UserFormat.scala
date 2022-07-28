package json

import json.JsonOps.FormatOps
import models.User.{Lecturer, Student}
import models.{User, UserJson, UserStatus}
import play.api.libs.json._

trait UserFormat {
  implicit val userStatusFmt: Format[UserStatus] =
    Format.of[String].bimapTry(UserStatus.apply, _.toString)

  implicit val userJsonFmt: OFormat[UserJson] =
    Json.format[UserJson]

  implicit val userFmt: OFormat[User] =
    OFormat.apply(
      js =>
        js.\("status")
          .validate[UserStatus]
          .flatMap[User] {
            case UserStatus.Student  => studentFmt.reads(js)
            case UserStatus.Lecturer => lecturerFmt.reads(js)
          },
      (user: User) =>
        user match {
          case l: Lecturer =>
            lecturerFmt
              .writes(l) + ("status" -> JsString(UserStatus.Lecturer.toString))
          case s: Student =>
            studentFmt
              .writes(s) + ("status" -> JsString(UserStatus.Student.toString))
        }
    )

  implicit val lecturerFmt: OFormat[Lecturer] = Json.format[Lecturer]

  implicit val studentFmt: OFormat[Student] = Json.format[Student]
}
