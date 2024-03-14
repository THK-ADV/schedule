package models

import controllers.JsonNullWritable
import play.api.libs.json.{JsObject, JsString, Json, Writes}

sealed trait Identity extends UniqueEntity[String] {
  def id: String
  def kind: String
  def username: Option[String]
  def fullName: String
}

object Identity extends JsonNullWritable {
  val PersonKind = "person"
  val GroupKind = "group"
  val UnknownKind = "unknown"

  case class Person(
      id: String,
      lastname: String,
      firstname: String,
      title: String,
      abbreviation: String,
      campusId: String
  ) extends Identity {
    override val kind = PersonKind
    override def username: Option[String] =
      Option.when(campusId.nonEmpty)(campusId)
    def fullName: String = s"$firstname $lastname"
  }

  case class Group(id: String, label: String) extends Identity {
    override val kind = GroupKind
    override def username: Option[String] = None
    override def fullName: String = label
  }

  case class Unknown(id: String, label: String) extends Identity {
    override val kind = UnknownKind
    override def username: Option[String] = None
    override def fullName: String = label
  }

  def apply(
      id: String,
      campusId: String,
      firstname: String,
      lastname: String,
      kind: String,
      abbrev: String,
      title: String
  ): Identity = kind match {
    case PersonKind =>
      Identity.Person(id, lastname, firstname, title, abbrev, campusId)
    case GroupKind =>
      Identity.Group(id, campusId)
    case UnknownKind =>
      Identity.Unknown(id, campusId)
  }

  def unapply(p: Identity): Option[
    (String, String, String, String, String, String, String)
  ] = p match {
    case Identity.Person(
          id,
          lastname,
          firstname,
          title,
          abbreviation,
          campusId
        ) =>
      Some(
        id,
        campusId,
        firstname,
        lastname,
        PersonKind,
        abbreviation,
        title
      )
    case Identity.Group(id, label) =>
      Some(id, "", "", "", GroupKind, "", label)
    case Identity.Unknown(id, label) =>
      Some(id, "", "", "", UnknownKind, "", label)
  }

  private def unknownWrites: Writes[Unknown] =
    Json
      .writes[Unknown]
      .transform((js: JsObject) => js + ("kind" -> JsString(UnknownKind)))

  implicit def personWrites: Writes[Person] =
    Json
      .writes[Person]
      .transform((js: JsObject) => js + ("kind" -> JsString(PersonKind)))

  implicit def groupWrites: Writes[Group] =
    Json
      .writes[Group]
      .transform((js: JsObject) => js + ("kind" -> JsString(GroupKind)))

  implicit def writes: Writes[Identity] = {
    case single: Person =>
      personWrites.writes(single)
    case group: Group =>
      groupWrites.writes(group)
    case unknown: Unknown =>
      unknownWrites.writes(unknown)
  }
}
