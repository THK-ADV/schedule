package service

import database.{SQLDateConverter, SemesterRepository}
import models.{Semester, SemesterJson}

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class SemesterService @Inject() (repo: SemesterRepository)
    extends SQLDateConverter {

  def create(json: SemesterJson): Future[Semester] =
    if (json.end.isBefore(json.start))
      abort(
        s"semester start should be before semester end, but was ${json.start} - ${json.end}"
      )
    else
      repo.create(
        toSemester(json, None),
        t => List(t.onStart(json.start), t.onEnd(json.end))
      )

  def all(filter: Map[String, Seq[String]]): Future[Seq[Semester]] =
    repo.list(filter)

  def delete(id: UUID): Future[Semester] =
    repo.delete(id)

  def update(json: SemesterJson, id: UUID): Future[Semester] =
    repo.update(
      toSemester(json, Some(id)),
      ex => ex.start == json.start && ex.end == json.end
    )

  def toSemester(s: SemesterJson, id: Option[UUID]): Semester =
    Semester(
      s.label,
      s.abbreviation,
      s.start,
      s.end,
      s.examStart,
      id getOrElse UUID.randomUUID
    )

  def abort(s: String): Future[Semester] =
    Future.failed(new Throwable(s))
}
