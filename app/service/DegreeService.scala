package service

import database.repos.DegreeRepository
import models.Degree
import service.abstracts.Get

import javax.inject.{Inject, Singleton}

@Singleton
final class DegreeService @Inject() (val repo: DegreeRepository)
    extends Get[String, Degree]
