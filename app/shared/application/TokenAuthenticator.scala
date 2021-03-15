package shared.application

import shared.domain.ApiError.WrongBearer
import shared.domain.{ApiError, AuthenticatedContext}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TokenAuthenticator @Inject() (implicit ec: ExecutionContext) {

  def authenticateToken(bearer: String): Future[Either[ApiError, AuthenticatedContext]] = Future {
    if (bearer == "SecretKey") Right(AuthenticatedContext("JohnDoe")) else Left(WrongBearer)
  }

}
