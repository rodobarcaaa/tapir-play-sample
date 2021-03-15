package books.domain

import play.api.libs.json.{Format, Json}

import java.util.UUID

final case class BookId(uuid: UUID) extends AnyVal

object BookId {
  implicit val format: Format[BookId] = Json.valueFormat
}
