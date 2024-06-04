package localization

sealed trait Localization {
  def fold[A](de: => A, en: => A): A = this match {
    case Localization.De => de
    case Localization.En => en
  }
}

object Localization {
  case object De extends Localization
  case object En extends Localization
}
