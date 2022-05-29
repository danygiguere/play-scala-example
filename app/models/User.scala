package models
import com.google.inject.Inject
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.MySQLProfile.api._

case class User(id: Long, firstName: String, lastName: String, mobile: Long, email: String)

case class UserFormData(firstName: String, lastName: String, mobile: Long, email: String)

object UserForm {
  val form: Form[UserFormData] = Form(
    mapping(
      "firstName" -> text(minLength = 0, maxLength = 8),
      "lastName" -> nonEmptyText,
      "mobile" -> longNumber,
      "email" -> email
    )(UserFormData.apply)(UserFormData.unapply)
  )
}

class UserTableDef(tag: Tag) extends Table[User](tag, "user") {

  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def firstName = column[String]("first_name")
  def lastName = column[String]("last_name")
  def mobile = column[Long]("mobile")
  def email = column[String]("email")

  override def * =
    (id, firstName, lastName, mobile, email) <>(User.tupled, User.unapply)
}

class Users @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  val users = TableQuery[UserTableDef]

  def listAll: Future[Seq[User]] = {
    dbConfig.db.run(users.result)
  }

  def get(id: Long): Future[Option[User]] = {
    dbConfig.db.run(users.filter(_.id === id).result.headOption)
  }

  def add(user: User): Future[String] = {
    dbConfig.db.run(users += user).map(res => "User successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def update(user: User): Future[Int] = {
    dbConfig.db
      .run(users.filter(_.id === user.id)
        .map(x => (x.firstName, x.lastName, x.mobile, x.email))
        .update(user.firstName, user.lastName, user.mobile, user.email)
      )
  }

  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(users.filter(_.id === id).delete)
  }

}
