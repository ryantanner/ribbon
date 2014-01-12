package models

import play.api.Play.current
import play.api.db.slick.Config.driver.simple._

import slick.lifted.{ Join, MappedTypeMapper }
import scala.slick.lifted.ForeignKeyAction
import com.github.tototoshi.slick.JodaSupport._

import org.joda.time.DateTime

case class Stack(
  id:       Option[Long] = None,
  name:     String,
  version:  Int = 1,
  created:  DateTime = new DateTime,
  updated:  Option[DateTime]
)

trait StacksComponent {
  val Stacks: Stacks

  class Stacks extends Table[Stack]("stacks") with CRUDModel[Stack] {

    def id      = column[Long]("stack_id", O.PrimaryKey, O.AutoInc)
    def name    = column[String]("name")
    def version = column[Int]("version")
    def created = column[DateTime]("created_at")
    def updated = column[DateTime]("updated_at")

    def * = id.? ~ name ~ version ~ created ~ updated.? <> (Stack.apply _, Stack.unapply _)

  }
}
