package books.domain

import scala.concurrent.Future

trait BookRepository {

  def all(title: Option[String]): Future[Seq[Book]]

  def add(book: Book): Future[BookId]

  def get(id: BookId): Future[Option[Book]]

  def update(id: BookId, book: Book): Future[Unit]

  def delete(id: BookId): Future[Unit]

}
