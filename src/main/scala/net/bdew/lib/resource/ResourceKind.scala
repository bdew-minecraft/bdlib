package net.bdew.lib.resource

import net.bdew.lib.gui.{Color, Texture}
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.text.{IFormattableTextComponent, ITextComponent}
import net.minecraftforge.common.util.Constants

trait ResourceKind {
  def getTexture: Texture
  def getColor: Color
  def getName: ITextComponent
  def getFormattedString(amount: Double, capacity: Double): IFormattableTextComponent
  def capacityMultiplier: Double
  def helperObject: ResourceHelper[_ >: this.type]
  // Type bound is required because some co(ntra)variance bullshit that i don't fully understand
}

abstract class ResourceHelper[T <: ResourceKind](val id: String) {
  def loadFromNBT(tag: CompoundNBT): Option[T]
  def saveToNBT(tag: CompoundNBT, resource: T): Unit
}

object ResourceKind {
  def loadFromNBT(tag: CompoundNBT): Option[ResourceKind] = {
    if (tag.contains("kind", Constants.NBT.TAG_STRING)) {
      ResourceManager.resourceHelpers.get(tag.getString("kind")).flatMap(_.loadFromNBT(tag))
    } else None
  }

  def saveToNBT(tag: CompoundNBT, resource: ResourceKind): Unit = {
    tag.putString("kind", resource.helperObject.id)
    resource.helperObject.saveToNBT(tag, resource)
  }
}