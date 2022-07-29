package provider

import auth._
import org.keycloak.adapters.KeycloakDeploymentBuilder
import play.api.Environment
import play.api.libs.json.{JsValue, Json, Reads}
import play.api.libs.ws.WSClient

import javax.inject.{Inject, Provider, Singleton}
import scala.concurrent.ExecutionContext

case class UserToken(
    firstname: String,
    lastname: String,
    username: String,
    email: String
)

private class HttpClientImpl(
    ws: WSClient,
    implicit val ctx: ExecutionContext
) extends HttpClient[JsValue] {
  private val reads: Reads[KeycloakCert] =
    Json.reads[KeycloakCert]

  override def get(url: String) =
    ws.url(url).get().map(_.json)

  override def parseJson(json: JsValue) =
    (json \ "keys" \ 0).validate(reads).asOpt
}

private class KeycloakTokenExtractor extends AttributesExtractor[UserToken] {
  override def extract(attributes: Map[String, AnyRef], mail: String) =
    for {
      firstname <- attributes.get("firstname").map(_.toString)
      lastname <- attributes.get("lastname").map(_.toString)
      username <- attributes.get("username").map(_.toString)
    } yield UserToken(firstname, lastname, username, mail)
}

@Singleton
class OAuthAuthorizationProvider @Inject() (
    ws: WSClient,
    env: Environment,
    implicit val ctx: ExecutionContext
) extends Provider[OAuthAuthorization[UserToken]] {
  override def get(): OAuthAuthorization[UserToken] = {
    val keycloakDeployment = KeycloakDeploymentBuilder.build(
      env.classLoader.getResourceAsStream("keycloak.json")
    )
    val httpClient = new HttpClientImpl(ws, ctx)
    val tokenExtractor = new KeycloakTokenExtractor()
    new KeycloakAuthorization(
      httpClient,
      keycloakDeployment,
      tokenExtractor,
      ctx
    )
  }
}
