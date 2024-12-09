package ops

import play.api.Configuration

object ConfigurationOps {
  final implicit class Ops(private val self: Configuration) extends AnyVal {
    def nonEmptyString(key: String): String =
      self.getOptional[String](key) match {
        case Some(value) if value.nonEmpty => value
        case other =>
          throw new Throwable(
            s"expected a non empty string for key $key, but found $other"
          )
      }
  }
}
