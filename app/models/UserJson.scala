package models

case class UserJson(
    username: String,
    firstname: String,
    lastname: String,
    status: UserStatus,
    email: String,
    title: Option[String],
    initials: Option[String]
)
