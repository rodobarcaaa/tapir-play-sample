package shared.infrastructure

import shared.application.TokenAuthenticator
import shared.domain.{ApiError, AuthenticatedContext}
import sttp.model.StatusCode._
import sttp.tapir._
import sttp.tapir.json.play.jsonBody
import sttp.tapir.server.PartialServerEndpoint
import sttp.tapir.generic.auto._

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class SecuredEndpoints @Inject() (tokenAuthenticator: TokenAuthenticator) {

  private val securedWithBearerEndpoint: Endpoint[String, ApiError, Unit, Any] = endpoint
    .in(auth.bearer[String]())
    .errorOut(statusCode(Unauthorized))
    .errorOut(jsonBody[ApiError])

  val securedWithBearer: PartialServerEndpoint[AuthenticatedContext, Unit, ApiError, Unit, Any, Future] =
    securedWithBearerEndpoint.serverLogicForCurrent(tokenAuthenticator.authenticateToken)

}
