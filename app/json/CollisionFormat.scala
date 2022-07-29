package json

import collision.Collision
import play.api.libs.json.{Json, Writes}

trait CollisionFormat extends CollisionTypeFormat with ScheduleFormat {
  implicit val collisionWrites: Writes[Collision] =
    Json.writes[Collision]
}
