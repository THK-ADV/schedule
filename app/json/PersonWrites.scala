package json

import models.Person
import play.api.libs.json._

trait PersonWrites extends JsonNullWritable {

  implicit val writes: Writes[Person] = {
    Writes.apply {
      case p: Person.Default => defaultWrites.writes(p)
      case p: Person.Group   => groupWrites.writes(p)
      case p: Person.Unknown => unknownWrites.writes(p)
    }
  }

  private val defaultWrites: Writes[Person.Default] =
    Json.writes.transform((js: JsObject) =>
      js + ("kind" -> JsString(Person.DefaultKind))
    )

  private val groupWrites: Writes[Person.Group] =
    Json.writes.transform((js: JsObject) =>
      js + ("kind" -> JsString(Person.GroupKind))
    )

  private val unknownWrites: Writes[Person.Unknown] =
    Json.writes.transform((js: JsObject) =>
      js + ("kind" -> JsString(Person.UnknownKind))
    )
}
