package books

import shared.domain.ApiError

package object domain {
  val BookNotFound: ApiError = ApiError(404, "Book Not Found")
}
