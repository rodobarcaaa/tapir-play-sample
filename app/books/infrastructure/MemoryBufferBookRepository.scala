package books.infrastructure

import books.domain._

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MemoryBufferBookRepository @Inject() (implicit ec: ExecutionContext) extends BookRepository {

  private val books: mutable.Buffer[Book] = Seq(
    Book("The Sorrows of Young Werther", 1774, Author("Johann Wolfgang von Goethe")),
    Book("Iliad", 1778, Author("Homer")),
    Book("Nad Niemnem", 1888, Author("Eliza Orzeszkowa")),
    Book("The Colour of Magic", 1983, Author("Terry Pratchett")),
    Book("The Art of Computer Programming", 1968, Author("Donald Knuth")),
    Book("Pharaoh", 1897, Author("Boleslaw Prus"), BookId(UUID.fromString("de12d318-7407-4dc5-8c32-8020a8bc4710")))
  ).toBuffer

  override def all(title: Option[String]): Future[Seq[Book]] = Future {
    title match {
      case Some(t) if t.nonEmpty => books.toSeq.filter(_.title == t)
      case _                     => books.toSeq
    }
  }

  override def add(book: Book): Future[BookId] = Future {
    books += book
    book.id
  }

  override def get(id: BookId): Future[Option[Book]] = Future {
    books.find(_.id == id)
  }

  override def update(id: BookId, book: Book): Future[Unit] = Future {
    val index = books.indexWhere(_.id == id)
    if (index != -1) books.update(index, book)
  }

  override def delete(id: BookId): Future[Unit] = Future {
    val maybeBook = books.find(_.id == id)
    maybeBook.foreach(books.-=)
  }

}
