package service

import javax.inject.Inject
import javax.inject.Singleton

import database.repos.SeasonRepository
import models.Season
import service.abstracts.Get

@Singleton
final class SeasonService @Inject() (val repo: SeasonRepository) extends Get[String, Season]
