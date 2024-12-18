package service

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext

import database.repos.SemesterRepository
import models.Semester
import service.abstracts.Create
import service.abstracts.Get

@Singleton
final class SemesterService @Inject() (
    val repo: SemesterRepository,
    implicit val ctx: ExecutionContext
) extends Get[UUID, Semester]
    with Create[UUID, Semester] {

  protected override def validate(elem: Semester) =
    Option
      .when(
        elem.end.isBefore(elem.start) && elem.lectureEnd.isBefore(
          elem.lectureStart
        )
      )(
        new Throwable(
          s"semester start should be before semester end, but was ${elem.start} - ${elem.end}"
        )
      )
      .orElse(
        Option.unless(
          elem.deLabel.startsWith("Sommersemester") || elem.deLabel.startsWith(
            "Wintersemester"
          )
        )(
          new Throwable(
            s"semester label must start with 'Sommersemester' or 'Wintersemester', but was ${elem.deLabel}"
          )
        )
      )
}
