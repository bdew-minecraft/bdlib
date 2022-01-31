package net.bdew.lib.resource

import net.bdew.lib.gui.{Color, Texture}
import net.minecraft.nbt.{CompoundTag, Tag}
import net.minecraft.network.chat.{Component, MutableComponent}

trait ResourceKind {
  def getTexture: Texture
  def getColor: Color
  def getName: Component
  def getFormattedString(amount: Double, capacity: Double): MutableComponent
  def capacityMultiplier: Double
  def helperObject: ResourceHelper[_ >: this.type]
  // Type bound is required because some co(ntra)variance bullshit that i don't fully understand
}

abstract class ResourceHelper[T <: ResourceKind](val id: String) {
  def loadFromNBT(tag: CompoundTag): Option[T]
  def saveToNBT(tag: CompoundTag, resource: T): Unit
}

object ResourceKind {
  def loadFromNBT(tag: CompoundTag): Option[ResourceKind] = {
    if (tag.contains("kind", Tag.TAG_STRING)) {
      ResourceManager.resourceHelpers.get(tag.getString("kind")).flatMap(_.loadFromNBT(tag))
    } else None
  }

  def saveToNBT(tag: CompoundTag, resource: ResourceKind): Unit = {
    tag.putString("kind", resource.helperObject.id)
    resource.helperObject.saveToNBT(tag, resource)
  }
}