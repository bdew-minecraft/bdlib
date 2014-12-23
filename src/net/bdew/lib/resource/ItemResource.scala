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
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.NBTTagCompound

case class ItemResource(item: Item, meta: Int) extends ResourceKind {
  override def getTexture = new IconWrapper(
    if (item.getSpriteNumber == 0) Texture.BLOCKS else Texture.ITEMS,
    item.getIconFromDamageForRenderPass(meta, 0))

  override def getColor = Color.fromInt(item.getColorFromItemStack(makeStack(1), 0))

  override def getLocalizedName = item.getItemStackDisplayName(makeStack(1))
  override def getUnlocalizedName = item.getUnlocalizedName(makeStack(1))

  override def getFormattedString(amount: Double, capacity: Double) =
    Misc.toLocalF("resource.item.format", DecFormat.dec2(amount), DecFormat.round(capacity))

  override def capacityMultiplier = 1 / 250D

  def makeStack(n: Int) = new ItemStack(item, n, meta)

  override def toString = "ItemResource(%s,%d)".format(getUnlocalizedName, meta)
  override def helperObject = ItemResourceHelper
}

object ItemResourceHelper extends ResourceHelper[ItemResource]("item") {
  override def loadFromNBT(tag: NBTTagCompound) = {
    val item = Option(Item.getItemById(tag.getInteger("id")))
    val meta = tag.getInteger("meta")
    item.map(ItemResource(_, meta))
  }
  override def saveToNBT(tag: NBTTagCompound, r: ItemResource) {
    tag.setInteger("id", Item.getIdFromItem(r.item))
    tag.setInteger("meta", r.meta)
  }
}

