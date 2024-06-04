package service

import database.repos.StudyProgramRepository
import models.StudyProgram
import service.abstracts.Get

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
final class StudyProgramService @Inject() (val repo: StudyProgramRepository)
    extends Get[UUID, StudyProgram]
