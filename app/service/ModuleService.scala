package service

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

import database.repos.ModuleRepository
import models.Module
import service.abstracts.Get

@Singleton
final class ModuleService @Inject() (val repo: ModuleRepository) extends Get[UUID, Module]
