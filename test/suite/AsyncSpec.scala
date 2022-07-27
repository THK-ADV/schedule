package suite

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait AsyncSpec extends UnitSpec {
  def await[A](f: Future[A]) = Await.result(f, Duration.Inf)
}
