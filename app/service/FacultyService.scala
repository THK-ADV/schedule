package service

import javax.inject.Inject
import javax.inject.Singleton

import database.repos.FacultyRepository
import models.Faculty
import service.abstracts.Get

@Singleton
final class FacultyService @Inject() (val repo: FacultyRepository) extends Get[String, Faculty]
