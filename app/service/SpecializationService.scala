package service

import javax.inject.Inject
import javax.inject.Singleton

import database.repos.SpecializationRepository
import models.Specialization
import service.abstracts.Get

@Singleton
final class SpecializationService @Inject() (val repo: SpecializationRepository) extends Get[String, Specialization]
