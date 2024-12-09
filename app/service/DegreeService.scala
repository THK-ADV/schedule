package service

import javax.inject.Inject
import javax.inject.Singleton

import database.repos.DegreeRepository
import models.Degree
import service.abstracts.Get

@Singleton
final class DegreeService @Inject() (val repo: DegreeRepository) extends Get[String, Degree]
