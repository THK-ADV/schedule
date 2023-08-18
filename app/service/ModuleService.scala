package service

import database.repos.ModuleRepository
import models.Module
import service.abstracts.Get

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
final class ModuleService @Inject() (val repo: ModuleRepository)
    extends Get[UUID, Module]
