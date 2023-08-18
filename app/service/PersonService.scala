package service

import database.repos.PersonRepository
import models.Person
import service.abstracts.Get

import javax.inject.{Inject, Singleton}

@Singleton
final class PersonService @Inject() (val repo: PersonRepository)
    extends Get[String, Person]
