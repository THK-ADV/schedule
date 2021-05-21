package models

import play.api.libs.json.{Json, OFormat}

case class UserJson(
    username: String,
    firstname: String,
    lastname: String,
    status: String,
    email: String,
    title: Option[String],
    initials: Option[String]
)

object UserJson {
  implicit val format: OFormat[UserJson] = Json.format[UserJson]
}
