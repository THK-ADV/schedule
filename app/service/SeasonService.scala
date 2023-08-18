package service

import database.repos.SeasonRepository
import models.Season
import service.abstracts.Get

import javax.inject.{Inject, Singleton}

@Singleton
final class SeasonService @Inject() (val repo: SeasonRepository)
    extends Get[String, Season]
