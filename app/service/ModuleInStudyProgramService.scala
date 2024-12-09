package service

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

import database.repos.ModuleInStudyProgramRepository
import models.ModuleInStudyProgram
import service.abstracts.Get

@Singleton
final class ModuleInStudyProgramService @Inject() (
    val repo: ModuleInStudyProgramRepository
) extends Get[UUID, ModuleInStudyProgram]
