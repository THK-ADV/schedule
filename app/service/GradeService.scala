package service

import database.repos.GradeRepository
import models.Grade
import service.abstracts.Get

import javax.inject.{Inject, Singleton}

@Singleton
final class GradeService @Inject() (val repo: GradeRepository)
    extends Get[String, Grade]
