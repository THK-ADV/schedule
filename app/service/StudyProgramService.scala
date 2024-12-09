package service

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

import database.repos.StudyProgramRepository
import models.StudyProgram
import service.abstracts.Get

@Singleton
final class StudyProgramService @Inject() (val repo: StudyProgramRepository) extends Get[UUID, StudyProgram]
