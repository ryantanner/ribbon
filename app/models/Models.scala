package models

import play.api.Play.current
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB

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

/**
 * Helper for otherwise verbose Slick model definitions
 */
trait CRUDModel[T <: AnyRef { val id: Option[Long] }] { self: Table[T] =>
 
  def id: Column[Long]
 
  def * : scala.slick.lifted.ColumnBase[T]
 
  def autoInc = * returning id
 
  def insert(entity: T) = {
    DB.withSession { implicit session: Session =>
      autoInc.insert(entity)
    }
  }
 
  def insertAll(entities: Seq[T]) {
    DB.withSession { implicit session: Session =>
      autoInc.insertAll(entities: _*)
    }
  }
 
  def update(id: Long, entity: T) {
    DB.withSession { implicit session: Session =>
      tableQueryToUpdateInvoker(
        tableToQuery(this).where(_.id === id)
      ).update(entity)
    }
  }
 
  def delete(id: Long) {
    DB.withSession { implicit session: Session =>
      queryToDeleteInvoker(
        tableToQuery(this).where(_.id === id)
      ).delete
    }
  }
 
  def count = DB.withSession { implicit session: Session =>
    Query(tableToQuery(this).length).first
  }

  // ResourceType type mapper
  implicit val resourceTypeMapper = MappedTypeMapper.base[ResourceType, String](
    { r => if(r == EC2) "EC2" else "" },
    { s => EC2 }
  )
 
}

// Resource Types
sealed trait ResourceType
case object EC2 extends ResourceType

