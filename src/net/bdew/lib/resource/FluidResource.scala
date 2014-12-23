/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.resource

import net.bdew.lib.gui.{Color, IconWrapper, Texture}
import net.bdew.lib.{DecFormat, Misc}
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fluids.{Fluid, FluidRegistry, FluidStack}

case class FluidResource(fluid: Fluid) extends ResourceKind {
  override def getTexture = new IconWrapper(Texture.BLOCKS, fluid.getIcon)
  override def getColor = Color.fromInt(fluid.getColor)
  override def getLocalizedName = fluid.getLocalizedName(new FluidStack(fluid, 1))
  override def getUnlocalizedName = fluid.getUnlocalizedName
  override def getFormattedString(amount: Double, capacity: Double) =
    Misc.toLocalF("resource.fluid.format", DecFormat.round(amount), DecFormat.round(capacity))
  override def capacityMultiplier = 1
  override def toString = "FluidResource(%s)".format(getUnlocalizedName)
  override def helperObject = FluidResourceHelper
}

object FluidResourceHelper extends ResourceHelper[FluidResource]("fluid") {
  override def loadFromNBT(tag: NBTTagCompound) = {
    if (tag.hasKey("fluid")) {
      val fName = tag.getString("fluid")
      Option(FluidRegistry.getFluid(fName)) map FluidResource
    } else None
  }
  override def saveToNBT(tag: NBTTagCompound, r: FluidResource) {
    tag.setString("fluid", r.fluid.getName)
  }
}
