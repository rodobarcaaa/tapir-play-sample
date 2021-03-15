package books.infrastructure

import akka.stream.Materializer
import books.application.BookService
import books.domain._
import books.infrastructure.ApiEndpoints._
import play.api.Logging
import play.api.routing.Router.Routes
import shared.domain.{ApiError, AuthenticatedContext}
import shared.infrastructure.SecuredEndpoints
import sttp.model.StatusCode._
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.play._
import sttp.tapir.server.play.PlayServerInterpreter

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}

@Singleton
class ApiEndpoints @Inject() (
    securedEndpoints: SecuredEndpoints,
    bookService: BookService
)(implicit val m: Materializer, ec: ExecutionContext)
    extends Logging {

  // ---------Init----------- //

  private val service     = "Books"
  private val resource    = service.toLowerCase
  private val base        = endpoint.tag(service).in(resource)
  private val baseSecured = securedEndpoints.securedWithBearer.tag(service).in(resource)
  private val pathId      = path[BookId]("id")
  private val book        = Book(title = "Pride and Prejudice", year = 1813, author = Author(name = "Jane Austen"))
  private val bodyExample = jsonBody[Book].description("The book to add").example(book)

  // ---------All------------ //

  private val allEndpoint = base.get
    .summary("List all books")
    .in(query[Option[String]]("title").description("The title to look for"))
    .out(jsonBody[Seq[Book]])

  private val allRoute = PlayServerInterpreter.toRoute {
    allEndpoint.serverLogic { title =>
      logger.trace(s"List all books" + title.fold("")(t => s", filter by title: $t"))
      bookService.all(title)
    }
  }

  // ---------Post----------- //

  private val postPartial = baseSecured.post
    .summary("Add a book")
    .in(bodyExample)
    .out(jsonBody[BookId])
    .out(statusCode(Created))

  private val postEndpoint = postPartial.endpoint

  private val postRoute: Routes = PlayServerInterpreter.toRoute {
    postPartial.serverLogic { case (ac: AuthenticatedContext, book: Book) =>
      logger.trace(s"Add book: $book with user: ${ac.userId}")
      bookService.add(book)
    }
  }

  // ---------Get------------ //

  private val getEndpoint = base.get
    .summary("Get a book (by id)")
    .in(pathId)
    .out(jsonBody[Book].description("The book (if found)"))
    .errorOut(statusCode(NotFound))
    .errorOut(jsonBody[ApiError])

  private val getRoute: Routes = PlayServerInterpreter.toRoute {
    getEndpoint.serverLogic { id =>
      logger.trace(s"Get book with id: $id")
      bookService.get(id)
    }
  }

  // ---------Put------------ //

  private val putPartial = baseSecured.put
    .summary("Update a book (by id)")
    .in(pathId)
    .in(bodyExample)
    .out(statusCode(NoContent))

  private val putEndpoint = putPartial.endpoint

  private val putRoute: Routes = PlayServerInterpreter.toRoute {
    putPartial.serverLogic { case (ac, (id, book)) =>
      logger.trace(s"Update book: ($id, $book) with user: ${ac.userId}")
      bookService.put(id, book)
    }
  }

  // ---------Delete--------- //

  private val deletePartial = baseSecured.delete
    .summary("Delete a book (by id)")
    .in(pathId)
    .out(statusCode(NoContent))

  private val deleteEndpoint = deletePartial.endpoint

  private val deleteRoute: Routes = PlayServerInterpreter.toRoute {
    deletePartial.serverLogic { case (ac, id) =>
      logger.trace(s"Delete book: $id with user: ${ac.userId}")
      bookService.delete(id)
    }
  }

  // ---------Expose--------- //

  val docs: List[Endpoint[_, _, _, _]] = List(allEndpoint, postEndpoint, getEndpoint, putEndpoint, deleteEndpoint)

  val routes: List[Routes] = List(allRoute, postRoute, getRoute, putRoute, deleteRoute)
}

object ApiEndpoints {
  import sttp.tapir.Codec.PlainCodec

  def decode(s: String): DecodeResult[BookId] = Try(BookId(UUID.fromString(s))) match {
    case Success(v) => DecodeResult.Value(v)
    case Failure(f) => DecodeResult.Error(s, f)
  }

  implicit val myIdCodec: PlainCodec[BookId] = Codec.string.mapDecode(decode)(_.uuid.toString)
}
