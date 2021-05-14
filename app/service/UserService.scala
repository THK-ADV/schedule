package service

import database.repos.UserRepository
import database.tables.{UserDbEntry, UserTable}
import models.{User, UserJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class UserService @Inject() (val repo: UserRepository)
    extends Service[UserJson, User, UserDbEntry, UserTable] {

  override protected def toUniqueDbEntry(json: UserJson, id: Option[UUID]) =
    UserDbEntry(
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

  override protected def validate(json: UserJson) = Some(
    new Throwable("user creation is not allowed")
  )

  override protected def uniqueCols(json: UserJson) = Nil
}
