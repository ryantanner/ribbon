package models

import play.api.Play.current
import play.api.db.slick.Config.driver.simple._

import slick.lifted.{ Join, MappedTypeMapper }
import scala.slick.lifted.ForeignKeyAction
import com.github.tototoshi.slick.JodaSupport._

import org.joda.time.DateTime


case class User( 
  id             : Option[Long] = None,
	name           : String,
	email          : String,
	password       : String,
	created        : DateTime = new DateTime(),
	firstLogin     : Option[DateTime],
	lastLogin      : Option[DateTime],
	passwordChanged: Option[DateTime],
	failedAttempts : Int = 0
) {

	def username = email

}

trait UsersComponent {
  val Users: Users

  class Users extends Table[User]("users") with CRUDModel[User] {
    def id = column[Long]("user_id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name", O.NotNull)
    def email = column[String]("email")
    def password = column[String]("password")
    def created = column[DateTime]("created_at", O.NotNull)
    def firstLogin = column[DateTime]("first_login")
    def lastLogin = column[DateTime]("last_login")
    def passwordChanged = column[DateTime]("password_changed")
    def failedAttempts = column[Int]("failed_attempts")
    def * = id.? ~ name ~ email ~ password ~ created ~ firstLogin.? ~ lastLogin.? ~ passwordChanged.? ~ failedAttempts <> (User.apply _,
      User.unapply _)
  }

}
