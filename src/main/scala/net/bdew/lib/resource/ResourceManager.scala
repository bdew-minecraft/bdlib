package net.bdew.lib.resource

object ResourceManager {
  var resourceHelpers = Map.empty[String, ResourceHelper[_ <: ResourceKind]]
  def register[T <: ResourceKind](helper: ResourceHelper[T]): Unit = resourceHelpers += helper.id -> helper

  register(FluidResourceHelper)
  register(ItemResourceHelper)
}
