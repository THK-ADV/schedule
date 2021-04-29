package service

import database.repos.UserRepository
import database.tables.UserTable
import models.{User, UserJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class UserService @Inject() (val repo: UserRepository)
    extends Service[UserJson, User, UserTable] {

  override protected def toModel(json: UserJson, id: Option[UUID]) =
    User(
      json.firstname,
      json.lastname,
      json.status,
      json.email,
      json.title,
      json.initials,
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: UserJson,
      existing: User
  ): Boolean = false

  override protected def uniqueCols(
      json: UserJson,
      table: UserTable
  ) = List.empty

  override protected def validate(json: UserJson) = Some(
    new Throwable("user creation is not allowed")
  )
}
