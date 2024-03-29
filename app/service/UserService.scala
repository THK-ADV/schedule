package service

import database.repos.UserRepository
import database.tables.{UserDbEntry, UserTable}
import models.{User, UserJson, UserStatus}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class UserService @Inject() (val repo: UserRepository)
    extends Service[UserJson, User, UserDbEntry, UserTable] {

  override protected def toUniqueDbEntry(json: UserJson, id: Option[UUID]) =
    UserDbEntry(
      json.username,
      json.firstname,
      json.lastname,
      json.status,
      json.email,
      json.title,
      json.initials,
      now(),
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: UserJson,
      existing: UserDbEntry
  ): Boolean = false

  def allLecturer() =
    all(
      Map("status" -> Seq(UserStatus.Lecturer.toString)),
      atomic = false
    )
}
