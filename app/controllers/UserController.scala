package controllers

import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import models.{User, UserForm, Users}
import play.api.libs.json.{Json, OFormat}

import javax.inject.Inject

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserController @Inject()(ControllerComponents: ControllerComponents, users: Users) extends AbstractController(ControllerComponents) {

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
    UserForm.form.bindFromRequest.fold(
      errorForm => {
        errorForm.errors.foreach(println)
        Future.successful(BadRequest("Error!"))
      },
      data => {
        val user = User(0, data.firstName, data.lastName, data.mobile, data.email)
        users.add(user).map( _ => Redirect(routes.UserController.index()))
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
        users.update(user).map( _ => Redirect(routes.UserController.index()))
      })
  }

  def delete(id: Long) = Action.async { implicit request: Request[AnyContent] =>
    users.delete(id) map { res =>
      Redirect(routes.UserController.index())
    }
  }

}