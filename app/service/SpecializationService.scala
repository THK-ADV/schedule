package service

import database.repos.SpecializationRepository
import models.Specialization
import service.abstracts.Get

import javax.inject.{Inject, Singleton}

@Singleton
final class SpecializationService @Inject() (val repo: SpecializationRepository)
    extends Get[String, Specialization]
