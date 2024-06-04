package service

import database.repos.FacultyRepository
import models.Faculty
import service.abstracts.Get

import javax.inject.{Inject, Singleton}

@Singleton
final class FacultyService @Inject() (val repo: FacultyRepository)
    extends Get[String, Faculty]
