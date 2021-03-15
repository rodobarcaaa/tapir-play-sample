package shared.infrastructure

import akka.stream.Materializer
import books.infrastructure.ApiEndpoints
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.openapi.circe.yaml._
import sttp.tapir.server.play._
import sttp.tapir.swagger.play.SwaggerPlay

import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class ApiRouter @Inject() (
    bookEndpoints: ApiEndpoints
)(implicit val m: Materializer, ec: ExecutionContext)
    extends SimpleRouter {

  private val openApiYml = {
    val endpoints = bookEndpoints.docs // ++ n
    val version   = Option(classOf[ApiRouter].getPackage.getImplementationVersion).getOrElse("DEVELOP")
    OpenAPIDocsInterpreter.toOpenAPI(endpoints, "Tapir Play Sample", version).toYaml
  }

  override def routes: Routes = new SwaggerPlay(openApiYml).routes

}
