package controllers

import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import models.{User, UserForm, Users}
import play.api.libs.json.{Json, OFormat}
import play.api.Logging

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class UserController @Inject()(ControllerComponents: ControllerComponents, users: Users) extends AbstractController(ControllerComponents) with Logging {

  implicit val userFormat: OFormat[User] = Json.format[User]

  def index() = Action.async { implicit request: Request[AnyContent] =>
    users.listAll map { items =>
      Ok(Json.toJson(items))
    }
  }

  def show(id: Long) = Action.async { implicit request: Request[AnyContent] =>
    users.get(id) map { item =>
      Ok(Json.toJson(item))
    }
  }

  def add() = Action.async { implicit request: Request[AnyContent] =>
    logger.warn(s"Request: ${request.body}")
    UserForm.form.bindFromRequest.fold(
      errorForm => {
        errorForm.errors.foreach(println)
        Future.successful(BadRequest(errorForm.toString))
      },
      data => {
        val user = User(0, data.firstName, data.lastName, data.mobile, data.email)
        logger.warn(s"user: ${user}")
        users.add(user).map( item => Redirect(routes.UserController.show(item.id)))
      })
  }

  def update(id: Long) = Action.async { implicit request: Request[AnyContent] =>
    UserForm.form.bindFromRequest.fold(
      errorForm => {
        errorForm.errors.foreach(println)
        Future.successful(BadRequest("Error!"))
      },
      data => {
        val user = User(id, data.firstName, data.lastName, data.mobile, data.email)
        users.update(user).map( _ => Redirect(routes.UserController.show(user.id)))
      })
  }

  def delete(id: Long) = Action.async { implicit request: Request[AnyContent] =>
    users.delete(id) map { res =>
      Redirect(routes.UserController.show(id))
    }
  }

}