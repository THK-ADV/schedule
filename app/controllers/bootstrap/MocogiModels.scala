package controllers.bootstrap

import json.{JsonNullWritable, LocalDateFormat}
import org.joda.time.LocalDate
import play.api.libs.json.{JsValue, Json, Reads}

import java.util.UUID

case class POMocogi(
    abbrev: String,
    version: Int,
    program: String,
    date: LocalDate,
    dateTo: Option[LocalDate]
)

object POMocogi extends JsonNullWritable with LocalDateFormat {
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

case class ModuleMocogi(
    id: UUID,
    title: String,
    abbrev: String,
    language: String,
    season: String,
    moduleType: String,
    status: String,
    workload: Map[String, Int],
    moduleManagement: List[String],
    lecturers: List[String],
    po: PO,
    moduleRelation: Option[MocogiModuleRelation]
)

object ModuleMocogi extends JsonNullWritable {
  implicit def po: Reads[PO] = Json.reads

  implicit def poM: Reads[POMandatory] = Json.reads

  implicit def poO: Reads[POOptional] = Json.reads

  implicit def reads: Reads[ModuleMocogi] = Json.reads
}

case class StudyProgramMocogi(
    abbrev: String,
    grade: String,
    deLabel: String,
    enLabel: String,
    externalAbbreviation: String
)

object StudyProgramMocogi {
  implicit def reads: Reads[StudyProgramMocogi] = Json.reads[StudyProgramMocogi]
}
