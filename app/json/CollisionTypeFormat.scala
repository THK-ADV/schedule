package json

import collision.CollisionType
import json.JsonOps.FormatOps
import play.api.libs.json.Format

trait CollisionTypeFormat {
  implicit val collisionTypeFmt: Format[CollisionType] =
    Format.of[String].bimapTry(CollisionType.apply, _.label)
}
