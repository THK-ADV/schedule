package database

import models.Semester
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SemesterRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ec: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  private val semesters = TableQuery[SemesterTable]

  def create(
      semester: Semester,
      uniqueCols: SemesterTable => List[Rep[Boolean]]
  ): Future[Semester] =
    db.run {
      for {
        exists <- semesters
          .filter(uniqueCols(_).reduce(_ && _))
          .result
        create <-
          if (exists.isEmpty) semesters returning semesters += semester
          else DBIO.failed(ModelAlreadyExistsException(semester, exists.head))
      } yield create
    }

  def list(filter: Map[String, Seq[String]]): Future[Seq[Semester]] = {
    def performFilter(list: List[SemesterTable => Rep[Boolean]]) = list match {
      case x :: xs =>
        xs.foldLeft(semesters.filter(x)) { (acc, next) =>
          acc.filter(next)
        }
      case Nil => semesters
    }

    def run(q: Query[SemesterTable, Semester, Seq]) =
      db.run(q.result)

    parseFilter(filter)
      .map(performFilter _ andThen run)
      .getOrElse(Future.successful(Nil))
  }

  def delete(id: UUID): Future[Semester] = {
    val q = semesters.filter(_.id === id)

    db.run {
      for {
        xs <- q.result
        x <-
          if (xs.size == 1) DBIO.successful(xs.head)
          else
            DBIO.failed(
              new Throwable(s"None or more than one element found: $xs")
            )
        del <- q.delete if del > 0
      } yield x
    }
  }

  def update(
      semester: Semester,
      canUpdate: (Semester) => Boolean
  ): Future[Semester] = {
    val q = semesters.filter(_.id === semester.id)

    db.run {
      for {
        xs <- q.result
        existing <-
          if (xs.size == 1) DBIO.successful(xs.head)
          else
            DBIO.failed(
              new Throwable(s"None or more than one element found: $xs")
            )
        _ <-
          if (canUpdate(existing)) q.update(semester)
          else DBIO.failed(InvalidUpdateException(semester, existing))
      } yield semester
    }
  }

  def parseFilter(
      filter: Map[String, Seq[String]]
  ): Option[List[SemesterTable => Rep[Boolean]]] = {
    filter.foldLeft(Option.apply(List.empty[SemesterTable => Rep[Boolean]])) {
      case (xs, ("label", vs)) =>
        val f =
          (t: SemesterTable) => t.label.toLowerCase.inSet(vs.map(_.toLowerCase))
        xs.map(xs => f :: xs)
      case _ =>
        None
    }
  }
}
