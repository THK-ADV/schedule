package service

import javax.inject.Inject
import javax.inject.Singleton

import database.repos.IdentityRepository
import models.Identity
import service.abstracts.Get

@Singleton
final class PersonService @Inject() (val repo: IdentityRepository) extends Get[String, Identity]
