package shared.domain

import play.api.libs.json.{Format, Json}

case class ApiError(
    code: Int,
    message: String
)

object ApiError {

  import sttp.model.StatusCode._
//  val WrongApikey: ApiError = ApiError(Forbidden.code, "Wrong apikey")
  val WrongBearer: ApiError = ApiError(Unauthorized.code, "Wrong bearer")

  implicit val format: Format[ApiError] = Json.format
}
