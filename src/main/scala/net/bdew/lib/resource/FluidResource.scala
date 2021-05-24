/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.resource

import net.bdew.lib.Text
import net.bdew.lib.gui.{Color, Texture}
import net.minecraft.fluid.Fluid
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.{IFormattableTextComponent, ITextComponent}
import net.minecraftforge.common.util.Constants
import net.minecraftforge.registries.ForgeRegistries

case class FluidResource(fluid: Fluid) extends ResourceKind {
  override def getTexture: Texture = Texture.block(fluid.getAttributes.getStillTexture)
  override def getColor: Color = Color.fromInt(fluid.getAttributes.getColor)
  override def getName: ITextComponent = Text.translate(fluid.getAttributes.getTranslationKey)
  override def getFormattedString(amount: Double, capacity: Double): IFormattableTextComponent =
    Text.fluidCap(amount, capacity)
  override def capacityMultiplier = 1
  override def toString: String = "FluidResource(%s)".format(fluid.getRegistryName)
  override def helperObject: ResourceHelper[_ >: FluidResource.this.type] = FluidResourceHelper
}

object FluidResourceHelper extends ResourceHelper[FluidResource]("fluid") {
  override def loadFromNBT(tag: CompoundNBT): Option[FluidResource] = {
    if (tag.contains("fluid", Constants.NBT.TAG_STRING)) {
      val fName = tag.getString("fluid")
      Option(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fName))) map FluidResource
    } else None
  }
  override def saveToNBT(tag: CompoundNBT, r: FluidResource): Unit = {
    tag.putString("fluid", r.fluid.getRegistryName.toString)
  }
}
