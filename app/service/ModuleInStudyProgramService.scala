package service

import database.repos.ModuleInStudyProgramRepository
import models.ModuleInStudyProgram
import service.abstracts.Get

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
final class ModuleInStudyProgramService @Inject() (
    val repo: ModuleInStudyProgramRepository
) extends Get[UUID, ModuleInStudyProgram]
