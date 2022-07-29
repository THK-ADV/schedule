package json

import models.Room.{RoomAtom, RoomDefault}
import models.{Room, RoomJson}
import play.api.libs.json.{Json, OFormat, Writes}

trait RoomFormat extends CampusFormat {
  implicit val roomJsonFmt: OFormat[RoomJson] = Json.format[RoomJson]

  implicit val roomWrites: Writes[Room] = Writes.apply {
    case default: RoomDefault => roomDefaultWrites.writes(default)
    case atom: RoomAtom       => roomAtomWrites.writes(atom)
  }

  implicit val roomDefaultWrites: Writes[RoomDefault] = Json.writes[RoomDefault]

  implicit val roomAtomWrites: Writes[RoomAtom] = Json.writes[RoomAtom]
}
