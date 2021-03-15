package books.infrastructure

import akka.stream.Materializer
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter

import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class ApiRouter @Inject() (
    bookEndpoints: ApiEndpoints
)(implicit val m: Materializer, ec: ExecutionContext)
    extends SimpleRouter {
  override def routes: Routes = bookEndpoints.routes.reduce(_.orElse(_))
}
