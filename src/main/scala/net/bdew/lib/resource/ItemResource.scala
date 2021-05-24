/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.resource

import net.bdew.lib.gui.{Color, Texture}
import net.bdew.lib.{Client, Text}
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.{IFormattableTextComponent, ITextComponent}
import net.minecraftforge.api.distmarker.{Dist, OnlyIn}
import net.minecraftforge.registries.ForgeRegistries

case class ItemResource(item: Item) extends ResourceKind {
  override def getTexture: Texture =
    Texture.block(Client.minecraft.getItemRenderer.getItemModelShaper.getParticleIcon(makeStack(1)))

  @OnlyIn(Dist.CLIENT)
  override def getColor: Color = Color.fromInt(Client.itemColors.getColor(makeStack(1), 0))

  override def getName: ITextComponent = item.getDescription

  override def getFormattedString(amount: Double, capacity: Double): IFormattableTextComponent =
    Text.withCap(amount, capacity)

  override def capacityMultiplier: Double = 1 / 250D

  def makeStack(n: Int) = new ItemStack(item, n)

  override def toString: String = "ItemResource(%s)".format(item.getRegistryName)
  override def helperObject: ResourceHelper[_ >: ItemResource.this.type] = ItemResourceHelper
}

object ItemResourceHelper extends ResourceHelper[ItemResource]("item") {
  override def loadFromNBT(tag: CompoundNBT): Option[ItemResource] = {
    val item = Option(ForgeRegistries.ITEMS.getValue(new ResourceLocation(tag.getString("id"))))
    item.map(ItemResource)
  }
  override def saveToNBT(tag: CompoundNBT, r: ItemResource): Unit = {
    tag.putString("id", r.item.getRegistryName.toString)
  }
}

