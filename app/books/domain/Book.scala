package books.domain

import play.api.libs.json.{Format, Json}

import java.time.LocalDate
import java.util.UUID

final case class Book(
    title: String,
    year: Int,
    author: Author,
    id: BookId = BookId(UUID.randomUUID())
) {
  // TODO: Refined or validates cats
  require(title.nonEmpty, "Error: Title Empty")
  require(year >= 1449 && year <= LocalDate.now.getYear, "Error: Invalid Year")
  require(author.name.nonEmpty && author.name.charAt(0).isUpper, "Error: Invalid Author Name")
}

object Book {
  implicit val format: Format[Book] = Json.using[Json.WithDefaultValues].format
}
