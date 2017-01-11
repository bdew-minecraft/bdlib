/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.block

import java.util
import java.util.Random

import net.bdew.lib.nbt.NBT
import net.bdew.lib.tile.TileExtended
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{ItemBlock, ItemStack}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.{BlockPos, RayTraceResult}
import net.minecraft.world.{IBlockAccess, World}

/**
  * Mixin for blocks to keep their data when broken
  * Must have a TileEntity that extends TileKeepData
  */
trait BlockKeepData extends Block with HasItemBlock {
  this: HasTE[_ <: TileKeepData] =>

  override val itemBlockInstance: ItemBlock = new ItemBlockKeepData(this)

  def getSavedBlock(world: IBlockAccess, pos: BlockPos, state: IBlockState) = {
    val stack = new ItemStack(getItemDropped(state, new Random(), 0), 1, damageDropped(state))
    // Don't try to save if called with bogus world object
    if (!(world.isInstanceOf[World] && world.asInstanceOf[World].getChunkProvider == null))
      getTE(world, pos).foreach(_.saveToItem(stack))
    stack
  }

  override def getDrops(world: IBlockAccess, pos: BlockPos, state: IBlockState, fortune: Int) = {
    val drops = new util.ArrayList[ItemStack]()
    drops.add(getSavedBlock(world, pos, state))
    drops
  }

  override def removedByPlayer(state: IBlockState, world: World, pos: BlockPos, player: EntityPlayer, willHarvest: Boolean): Boolean =
    if (willHarvest)
      true
    else
      super.removedByPlayer(state, world, pos, player, willHarvest)

  override def harvestBlock(world: World, player: EntityPlayer, pos: BlockPos, state: IBlockState, te: TileEntity, stack: ItemStack): Unit = {
    super.harvestBlock(world, player, pos, state, te, stack)
    world.setBlockToAir(pos)
  }

  override def getPickBlock(state: IBlockState, target: RayTraceResult, world: World, pos: BlockPos, player: EntityPlayer) = {
    getSavedBlock(world, pos, state)
  }

  /**
    * Override for special actions on placing
    * ItemStack might or might not contain actual data
    * Called with the TE created and added to world, after onBlockPlacedBy & co
    */
  def restoreTileEntity(world: World, pos: BlockPos, is: ItemStack, player: EntityPlayer) =
    getTE(world, pos).loadFromItem(is)
}

/**
  * Mixin for tile entities to keep their data when broken
  * Block class has to extend BlockKeepData
  */
trait TileKeepData extends TileExtended {
  /**
    * Override to modify data that will be stored in an item
    *
    * @param t Automatically serialized data
    * @return Modified serialized data
    */
  def afterTileBreakSave(t: NBTTagCompound) = t

  /**
    * Override to modify data that will be load from an item
    * After the tile entity is fully constructed and added to world
    *
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
  override def placeBlockAt(stack: ItemStack, player: EntityPlayer, world: World, pos: BlockPos, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, newState: IBlockState) = {
    if (super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState)) {
      if (!world.isRemote && b.isInstanceOf[BlockKeepData])
        b.asInstanceOf[BlockKeepData].restoreTileEntity(world, pos, stack, player)
      true
    } else false
  }
}