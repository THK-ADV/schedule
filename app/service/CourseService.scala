package service

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext

import database.repos.CourseRepository
import models.Course
import service.abstracts.Create
import service.abstracts.Get

@Singleton
final class CourseService @Inject() (
    val repo: CourseRepository,
    implicit val ctx: ExecutionContext
) extends Get[UUID, Course]
    with Create[UUID, Course] {
  // TODO bootstrap only
  def deleteAll() = repo.deleteAll()
}
