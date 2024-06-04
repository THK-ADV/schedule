package controllers.bootstrap

import controllers.JsonNullWritable
import play.api.libs.json.{JsValue, Json, Reads}

import java.util.UUID

case class POMocogi(
    id: String,
    version: Int
)

object POMocogi extends JsonNullWritable {
  implicit def reads: Reads[POMocogi] = Json.reads[POMocogi]
}

case class SpecializationMocogi(
    abbrev: String,
    label: String,
    po: String
)

object SpecializationMocogi {
  implicit def reads: Reads[SpecializationMocogi] =
    Json.reads[SpecializationMocogi]
}

// TODO: moduleRelation

case class POMandatory(
    po: String,
    specialization: Option[String],
    recommendedSemester: List[Int]
)

case class POOptional(
    po: String,
    specialization: Option[String],
    recommendedSemester: List[Int]
    // TODO add when mocogi supports it
//    isFocus: Boolean
)

case class PO(mandatory: List[POMandatory], optional: List[POOptional])

sealed trait MocogiModuleRelation

object MocogiModuleRelation {
  case class Child(kind: String, parent: UUID) extends MocogiModuleRelation
  case class Parent(kind: String, children: List[UUID])
      extends MocogiModuleRelation

  def childReads: Reads[Child] = Json.reads

  def parentReads: Reads[Parent] = Json.reads

  implicit def reads: Reads[MocogiModuleRelation] =
    (js: JsValue) =>
      for {
        kind <- js.\("kind").validate[String]
        res <-
          if (kind == "child") childReads.reads(js) else parentReads.reads(js)
      } yield res
}

case class MocogiModule(
    id: UUID,
    metadata: MocogiMetadata
)

object MocogiModule {
  implicit def reads: Reads[MocogiModule] = Json.reads
}

case class MocogiMetadata(
    title: String,
    abbrev: String,
    language: String,
    season: String,
    workload: Map[String, Int],
    moduleManagement: List[String],
    lecturers: List[String],
    po: PO,
    moduleRelation: Option[MocogiModuleRelation],
    status: String,
    moduleType: String
)

object MocogiMetadata extends JsonNullWritable {
  implicit def po: Reads[PO] = Json.reads

  implicit def poM: Reads[POMandatory] = Json.reads

  implicit def poO: Reads[POOptional] = Json.reads

  implicit def reads: Reads[MocogiMetadata] = Json.reads
}

case class MocogiDegree(
    id: String,
    deLabel: String,
    enLabel: String,
    deDesc: String,
    enDesc: String
)

object MocogiDegree {
  implicit def reads: Reads[MocogiDegree] = Json.reads
}

case class MocogiSpecialization(
    id: String,
    deLabel: String,
    enLabel: String
)

object MocogiSpecialization {
  implicit def reads: Reads[MocogiSpecialization] = Json.reads
}

case class StudyProgramMocogi(
    id: String,
    deLabel: String,
    enLabel: String,
    po: POMocogi,
    degree: MocogiDegree,
    specialization: Option[MocogiSpecialization]
)

object StudyProgramMocogi {
  implicit def reads: Reads[StudyProgramMocogi] = Json.reads[StudyProgramMocogi]
}
