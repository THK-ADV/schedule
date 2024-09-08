package service

import database.repos.{
  ModuleRelationRepository,
  ModuleRepository,
  ModuleSupervisorRepository
}
import models.{Module, ModuleRelation, ModuleSupervisor}
import service.abstracts.Get

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class ModuleService @Inject() (
    val repo: ModuleRepository
) extends Get[UUID, Module]
