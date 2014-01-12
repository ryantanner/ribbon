/*
 * Copyright 2013 Marconi Lanna
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package models

import play.api.Play.current
import play.api.db.slick.Config.driver.simple._

import slick.lifted.{ Join, MappedTypeMapper }
import scala.slick.lifted.ForeignKeyAction
import com.github.tototoshi.slick.JodaSupport._

import org.joda.time.DateTime

/**
 * Data Access Object trait
 *
 *  Used to create the DAOs: Companies and Computers
 */
private[models] trait DAO extends UsersComponent {
  val Users = new Users 
}

case class Page[A](items: Seq[A], page: Int, offset: Long, total: Long) {
  lazy val prev = Option(page - 1).filter(_ >= 0)
  lazy val next = Option(page + 1).filter(_ => (offset + items.size) < total)
}

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

  class Users extends Table[User]("USERS") {
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
    def forInsert = * returning id
  }

}

object Users extends DAO {
   /**
   * Construct the Map[String,String] needed to fill a select options set
   */
  def options(implicit s: Session): Seq[(String, String)] = {
    val query = (for {
      user <- Users
    } yield (user.id, user.name)).sortBy(_._2)
    query.list.map(row => (row._1.toString, row._2))
  }

  /**
   * Insert a new user 
   * @param user
   */
  def insert(user: User)(implicit s: Session) {
    Users.forInsert.insert(user)
  }

}
