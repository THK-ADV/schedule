package controllers

import controllers.crud.{JsonHttpResponse, Read}
import json.PersonWrites
import models.Person
import play.api.mvc.{AbstractController, ControllerComponents}
import service.PersonService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class PersonController @Inject() (
    cc: ControllerComponents,
    val service: PersonService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Read[String, Person]
    with PersonWrites
    with JsonHttpResponse[Person]
