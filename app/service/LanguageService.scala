package service

import database.repos.LanguageRepository
import models.Language
import service.abstracts.Get

import javax.inject.{Inject, Singleton}

@Singleton
final class LanguageService @Inject() (val repo: LanguageRepository)
    extends Get[String, Language]
