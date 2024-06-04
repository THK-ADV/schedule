package service

import database.repos.IdentityRepository
import models.Identity
import service.abstracts.Get

import javax.inject.{Inject, Singleton}

@Singleton
final class PersonService @Inject() (val repo: IdentityRepository)
    extends Get[String, Identity]
