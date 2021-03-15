package books.application

import akka.actor.ActorSystem
import books.domain._
import shared.domain.ApiError

import javax.inject._
import scala.concurrent.Future

@Singleton
class BookService @Inject() (bookRepository: BookRepository)(implicit as: ActorSystem) {
  import as.dispatcher

  def all(title: Option[String]): Future[Either[Unit, Seq[Book]]] =
    bookRepository.all(title).map(Right(_))

  def add(book: Book): Future[Either[ApiError, BookId]] =
    bookRepository.add(book).map(Right(_))

  def get(id: BookId): Future[Either[ApiError, Book]] =
    bookRepository.get(id).map(_.toRight(BookNotFound))

  def put(id: BookId, book: Book): Future[Either[ApiError, Unit]] =
    bookRepository.update(id, book).map(Right(_))

  def delete(id: BookId): Future[Either[ApiError, Unit]] =
    bookRepository.delete(id).map(Right(_))
}
