package controllers

import akka.util.ByteString
import play.api.i18n.I18nSupport.RequestWithMessagesApi
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents, Request, ResponseHeader, Result}

import javax.inject.{Inject, Singleton}
import play.api.Logger
import play.api.i18n._
import play.api.http.HttpEntity

@Singleton
class DemoController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  val demoLogger: Logger = Logger("demoLogger")

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(Json.toJson("Hello world from DemoController@index"))
  }

  def simple(): Action[AnyContent] = Action {
    Result(
      header = ResponseHeader(200, Map.empty),
      body = HttpEntity.Strict(ByteString("Hello world!"), Some("text/plain"))
    )
  }

  def request() = Action { request =>
    demoLogger.info(s"Request: ${request.body}")
    Ok("Got request [" + request + "]")
  }

  def logger(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    demoLogger.info("Hello world from DemoController@logger")
    Ok(Json.toJson("Hello world from DemoController@logger"))
  }

  def translation(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val messages: Messages = request.messages
    val message: String    = messages("home.title")
    Ok(message)
  }
}
