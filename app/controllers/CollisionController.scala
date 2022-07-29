package controllers

import collision.{Collision, CollisionType}
import json._
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}
import service.CollisionService

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class CollisionController @Inject() (
    cc: ControllerComponents,
    private val service: CollisionService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with JsonHttpResponse[Collision]
    with CollisionFormat {
  case class CollisionChecker(
      scheduleIds: List[UUID],
      collisionTypes: Set[CollisionType]
  )

  def checkForCollisions() =
    Action(parse.json[CollisionChecker]).async { r =>
      okSeq(
        service.checkForCollisions(
          r.body.scheduleIds,
          r.body.collisionTypes,
          Set.empty
        )
      )
    }

  def collisionTypes() =
    Action(_ => Ok(Json.toJson(CollisionType.all())))

  implicit val collisionCheckerReads: Reads[CollisionChecker] =
    Json.reads[CollisionChecker]

  override protected implicit val writes: Writes[Collision] =
    collisionWrites
}
