package service

import database.repos.CourseRepository
import models.Course
import service.abstracts.{Create, Get}

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class CourseService @Inject() (
    val repo: CourseRepository,
    implicit val ctx: ExecutionContext
) extends Get[UUID, Course]
    with Create[Course] {
  // TODO bootstrap only
  def deleteAll() = repo.deleteAll()
}
