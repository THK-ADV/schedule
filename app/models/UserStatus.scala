package models

import scala.util.{Failure, Success, Try}

sealed trait UserStatus {
  override def toString = UserStatus.unapply(this)
}

object UserStatus {
  case object Student extends UserStatus
  case object Lecturer extends UserStatus

  def apply(label: String): Try[UserStatus] =
    label.toLowerCase match {
      case "student"  => Success(Student)
      case "lecturer" => Success(Lecturer)
      case other =>
        Failure(
          new Throwable(
            s"expected status to be either student or lecturer, but was $other"
          )
        )
    }

  def unapply(status: UserStatus): String =
    status match {
      case Student  => "student"
      case Lecturer => "lecturer"
    }
}
