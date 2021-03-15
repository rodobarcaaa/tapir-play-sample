package books.domain

import play.api.libs.json.{Format, Json}

final case class Author(name: String) extends AnyVal

object Author {
  implicit val format: Format[Author] = Json.valueFormat
}
