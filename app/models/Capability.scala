package models

import play.api.Play.current
import play.api.db.slick.Config.driver.simple._

import slick.lifted.{ Join, MappedTypeMapper }
import scala.slick.lifted.ForeignKeyAction
import com.github.tototoshi.slick.JodaSupport._

import org.joda.time.DateTime

case class Capability(
  id:           Option[Long] = None,
  name:         String,
  resourceType: ResourceType,
  stackId:      Long,
  created:      DateTime = new DateTime,
  updated:      Option[DateTime]
)

trait CapabilitiesComponent { self: StacksComponent =>

  val Capabilities: Capabilities

  class Capabilities extends Table[Capability]("capabilities") with CRUDModel[Capability] {

    def id            = column[Long]("capability_id", O.PrimaryKey, O.AutoInc)
    def name          = column[String]("name")
    def resourceType  = column[ResourceType]("resource_type")
    def stackId       = column[Long]("stack_id")
    def created       = column[DateTime]("created_at")
    def updated       = column[DateTime]("updated_at")

    def stack         = foreignKey("stack_fk", stackId, Stacks)(_.id)

    def * = id.? ~ name ~ resourceType ~ stackId ~ created ~ updated.? <> (Capability.apply _, Capability.unapply _)

  }

}
