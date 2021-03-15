package books.infrastructure.routers

import books.domain._
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.libs.json.Json
import play.api.mvc.AnyContentAsEmpty
import play.api.test.Helpers._
import play.api.test.{FakeHeaders, FakeRequest, Injecting}

import java.util.UUID

class BookEndpointsSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "ApiRouter" should {

    "get all books" in {
      val books = route(app, FakeRequest(GET, "/books")).get

      status(books) mustBe OK
      contentType(books) mustBe Some("application/json")
    }

    "get all books by title = Pharaoh" in {
      val books = route(app, FakeRequest(GET, "/books?title=Pharaoh")).get

      status(books) mustBe OK
      contentType(books) mustBe Some("application/json")
      contentAsJson(books) mustEqual
        Json.parse("""[
                     |  {
                     |    "title": "Pharaoh",
                     |    "year": 1897,
                     |    "author": "Boleslaw Prus",
                     |    "id": "de12d318-7407-4dc5-8c32-8020a8bc4710"
                     |  }
                     |]""".stripMargin)
    }

    "add a book with valid authentication" in {
      val id   = UUID.randomUUID()
      val book = Book("A new book", 2020, Author("John Doe"), BookId(id))

      val added = route(
        app,
        FakeRequest(
          POST,
          "/books",
          FakeHeaders(Seq("Authorization" -> "Bearer SecretKey")),
          Json.toJson(book)
        )
      ).get

      status(added) mustBe CREATED
      contentAsString(added) mustEqual s""""$id""""
    }

    "add a book with invalid authentication" in {
      val book = Book("A new book", 2020, Author("John Doe"))

      val added = route(
        app,
        FakeRequest(
          POST,
          "/books",
          FakeHeaders(Seq("Authorization" -> "Bearer BadKey")),
          Json.toJson(book)
        )
      ).get

      status(added) mustBe UNAUTHORIZED
    }

    "get a book by id" in {
      val get = route(app, FakeRequest(GET, "/books/de12d318-7407-4dc5-8c32-8020a8bc4710")).get

      status(get) mustBe OK
      contentType(get) mustBe Some("application/json")
      contentAsJson(get) mustEqual Json.parse(
        """{"title":"Pharaoh","year":1897,"author":"Boleslaw Prus","id":"de12d318-7407-4dc5-8c32-8020a8bc4710"}"""
      )
    }

    "get a book by id - not found" in {
      val get = route(app, FakeRequest(GET, "/books/3fa85f64-5717-4562-b3fc-2c963f66afa6")).get

      status(get) mustBe NOT_FOUND
      contentType(get) mustBe Some("application/json")
      contentAsJson(get) mustEqual Json.parse("""{"code": 404,"message": "Book Not Found"}""")
    }

    "update a book with valid authentication" in {
      val id   = UUID.fromString("de12d318-7407-4dc5-8c32-8020a8bc4711")
      val book = Book("A new book [UPDATE]", 2020, Author("John Doe"), BookId(id))

      val updated = route(
        app,
        FakeRequest(
          PUT,
          s"/books/$id",
          FakeHeaders(Seq("Authorization" -> "Bearer SecretKey")),
          Json.toJson(book)
        )
      ).get

      status(updated) mustBe NO_CONTENT
    }

    "update a book - not found" in {
      val id   = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6")
      val book = Book("A new book [UPDATE]", 2020, Author("John Doe"), BookId(id))

      val updated = route(
        app,
        FakeRequest(
          PUT,
          s"/books/$id",
          FakeHeaders(Seq("Authorization" -> "Bearer SecretKey")),
          Json.toJson(book)
        )
      ).get

      status(updated) mustBe NO_CONTENT
    }

    "update a book with invalid authentication" in {
      val id   = UUID.fromString("de12d318-7407-4dc5-8c32-8020a8bc4710")
      val book = Book("A new book [UPDATE]", 2020, Author("John Doe"), BookId(id))

      val updated = route(
        app,
        FakeRequest(
          PUT,
          s"/books/$id",
          FakeHeaders(Seq("Authorization" -> "Bearer BadKey")),
          Json.toJson(book)
        )
      ).get

      status(updated) mustBe UNAUTHORIZED
    }

    "delete a book with valid authentication" in {
      val deleted = route(
        app,
        FakeRequest(
          DELETE,
          "/books/de12d318-7407-4dc5-8c32-8020a8bc4710",
          FakeHeaders(Seq("Authorization" -> "Bearer SecretKey")),
          AnyContentAsEmpty
        )
      ).get

      status(deleted) mustBe NO_CONTENT
    }

    "delete a book with invalid authentication" in {
      val deleted = route(
        app,
        FakeRequest(
          DELETE,
          "/books/de12d318-7407-4dc5-8c32-8020a8bc4710",
          FakeHeaders(Seq("Authorization" -> "Bearer BadKey")),
          AnyContentAsEmpty
        )
      ).get

      status(deleted) mustBe UNAUTHORIZED
    }

  }

}
