/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.block

import java.util

import net.bdew.lib.nbt.NBT
import net.bdew.lib.tile.TileExtended
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.World

/**
 * Mixin for blocks to keep their data when broken
 * Must have a TileEntity that extends TileKeepData
 */
trait BlockKeepData extends Block with HasItemBlock {
  this: HasTE[_ <: TileKeepData] =>

  override val ItemBlockClass: Class[_ <: ItemBlockKeepData] = classOf[ItemBlockKeepData]

  def getSavedBlock(world: World, x: Int, y: Int, z: Int, metadata: Int) = {
    val stack = new ItemStack(getItemDropped(metadata, world.rand, 0), 1, damageDropped(metadata))
    getTE(world, x, y, z).saveToItem(stack)
    stack
  }

  override def getDrops(world: World, x: Int, y: Int, z: Int, metadata: Int, fortune: Int): util.ArrayList[ItemStack] = {
    val drops = new util.ArrayList[ItemStack]()
    drops.add(getSavedBlock(world, x, y, z, metadata))
    drops
  }

  override def removedByPlayer(world: World, player: EntityPlayer, x: Int, y: Int, z: Int, willHarvest: Boolean): Boolean =
    if (willHarvest)
      true
    else
      super.removedByPlayer(world, player, x, y, z, willHarvest)

  override def harvestBlock(world: World, player: EntityPlayer, x: Int, y: Int, z: Int, meta: Int): Unit = {
    super.harvestBlock(world, player, x, y, z, meta)
    world.setBlockToAir(x, y, z)
  }

  override def getPickBlock(target: MovingObjectPosition, world: World, x: Int, y: Int, z: Int, player: EntityPlayer): ItemStack = {
    getSavedBlock(world, x, y, z, world.getBlockMetadata(x, y, z))
  }

  /**
   * Override for special actions on placing
   * ItemStack might or might not contain actual data
   * Called with the TE created and added to world, after onBlockPlacedBy & co
   */
  def restoreTileEntity(world: World, x: Int, y: Int, z: Int, is: ItemStack, player: EntityPlayer) = getTE(world, x, y, z).loadFromItem(is)
}

/**
 * Mixin for tile entities to keep their data when broken
 * Block class has to extend BlockKeepData
 */
trait TileKeepData extends TileExtended {
  /**
   * Override to modify data that will be stored in an item
   * @param t Automatically serialized data
   * @return Modified serialized data
   */
  def afterTileBreakSave(t: NBTTagCompound) = t

  /**
   * Override to modify data that will be load from an item
   * After the tile entity is fully constructed and added to world
   * @param t Automatically serialized data
   * @return Modified serialized data
   */
  def beforeTileBreakLoad(t: NBTTagCompound) = t

  final def saveToItem(is: ItemStack): Unit = {
    if (!is.hasTagCompound) is.setTagCompound(new NBTTagCompound)
    is.getTagCompound.setTag("data", afterTileBreakSave(NBT.from(persistSave.trigger)))
  }

  final def loadFromItem(is: ItemStack): Unit = {
    if (is.hasTagCompound && is.getTagCompound.hasKey("data")) {
      persistLoad.trigger(beforeTileBreakLoad(is.getTagCompound.getCompoundTag("data")))
    }
  }
}

/**
 * Custom ItemBlock that handles loading data to the Tile Entity
 */
class ItemBlockKeepData(b: Block) extends ItemBlockTooltip(b) {
  override def placeBlockAt(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float, metadata: Int): Boolean = {
    if (super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata)) {
      if (!world.isRemote && b.isInstanceOf[BlockKeepData])
        b.asInstanceOf[BlockKeepData].restoreTileEntity(world, x, y, z, stack, player)
      true
    } else false
  }
}