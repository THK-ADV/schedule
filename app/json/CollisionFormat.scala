package json

import collision.Collision
import play.api.libs.json.{Json, Writes}

trait CollisionFormat { self: CollisionTypeFormat with ScheduleFormat =>
  implicit val collisionWrites: Writes[Collision] =
    Json.writes[Collision]
}

object CollisionFormat {
  trait All
      extends CollisionFormat
      with CollisionTypeFormat
      with ScheduleFormat.All
}
