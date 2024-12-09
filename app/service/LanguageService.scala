package service

import javax.inject.Inject
import javax.inject.Singleton

import database.repos.LanguageRepository
import models.Language
import service.abstracts.Get

@Singleton
final class LanguageService @Inject() (val repo: LanguageRepository) extends Get[String, Language]
