/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.resource

import net.bdew.lib.Misc
import net.bdew.lib.gui.{Color, Texture}
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fluids.FluidStack

trait ResourceKind {
  def getTexture: Texture
  def getColor: Color
  def getLocalizedName: String
  def getUnlocalizedName: String
  def getFormattedString(amount: Double, capacity: Double): String
  def capacityMultiplier: Double
  def helperObject: ResourceHelper[_ >: this.type]
  // Type bound is required because some co(ntra)variance bullshit that i don't fully understand
}

abstract class ResourceHelper[T <: ResourceKind](val id: String) {
  def loadFromNBT(tag: NBTTagCompound): Option[T]
  def saveToNBT(tag: NBTTagCompound, resource: T): Unit
}

object ResourceManager {
  var resourceHelpers = Map.empty[String, ResourceHelper[_ <: ResourceKind]]
  def register[T <: ResourceKind](helper: ResourceHelper[T]) = resourceHelpers += helper.id -> helper

  register(FluidResourceHelper)
  register(ItemResourceHelper)

  def loadFromNBT(tag: NBTTagCompound): Option[Resource] = {
    val rKind = if (tag.hasKey("kind")) {
      resourceHelpers.get(tag.getString("kind")).flatMap(_.loadFromNBT(tag))
    } else None
    rKind.map(Resource(_, tag.getDouble("amount")))
  }

  def saveToNBT(r: Resource) = {
    Misc.applyMutator(new NBTTagCompound) { tag =>
      r.kind.helperObject.saveToNBT(tag, r.kind)
      tag.setDouble("amount", r.amount)
      tag.setString("kind", r.kind.helperObject.id)
    }
  }
}

case class Resource(kind: ResourceKind, amount: Double)

object Resource {
  def from(fs: FluidStack) = Resource(FluidResource(fs.getFluid), fs.amount)
  def from(is: ItemStack) = Resource(ItemResource(is.getItem, is.getItemDamage), is.stackSize)
}