package net.bdew.lib.multiblock

case class ModuleType(id: String)

class ModuleTypes {
  var all = Map.empty[String, ModuleType]

  def define(id: String): ModuleType = {
    val tmp = ModuleType(id)
    all += id -> tmp
    tmp
  }
}
