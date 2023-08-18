package models

sealed trait ModuleType {
  def value: String
  override def toString = value
}

object ModuleType {
  case object Module extends ModuleType {
    override val value: String = "module"
  }
  case object GenericModule extends ModuleType {
    override val value: String = "generic_module"
  }

  def apply(value: String): ModuleType = value match {
    case "module"         => Module
    case "generic_module" => GenericModule
  }
}
