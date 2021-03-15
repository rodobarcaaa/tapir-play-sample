package books

import books.domain.BookRepository
import books.infrastructure.MemoryBufferBookRepository
import net.codingwell.scalaguice.ScalaModule

import javax.inject.Singleton

final class BooksModule extends ScalaModule {
  override def configure(): Unit = {
    bind[BookRepository].to[MemoryBufferBookRepository].in[Singleton]()
  }
}
