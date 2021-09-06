package models

import org.joda.time.LocalDate

sealed trait Weekday {
  def value: Int
}

object Weekday {
  object Monday extends Weekday {
    override def value = 1
  }

  object Tuesday extends Weekday {
    override def value = 2
  }

  object Wednesday extends Weekday {
    override def value = 3
  }

  object Thursday extends Weekday {
    override def value = 4
  }

  object Friday extends Weekday {
    override def value = 5
  }

  object Unknown extends Weekday {
    override def value = -1
  }

  def apply(value: Int): Weekday = value match {
    case 1 => Monday
    case 2 => Tuesday
    case 3 => Wednesday
    case 4 => Thursday
    case 5 => Friday
    case _ => Unknown
  }

  def apply(date: LocalDate): Weekday = apply(date.getDayOfWeek)
}
