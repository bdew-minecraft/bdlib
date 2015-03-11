/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.rotate

import java.util

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.block.Block
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.util.{IIcon, MathHelper}
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.common.util.ForgeDirection

/**
 * Basic logic for block rotation
 */
trait BaseRotatableBlock extends Block {

  /**
   * Override this method to provide your textures, also called when rendering as item
   */
  def getIcon(meta: Int, kind: IconType.Value): IIcon

  /**
   * Override this method to provide your textures, if you need to check other state
   */
  def getIcon(world: IBlockAccess, x: Int, y: Int, z: Int, kind: IconType.Value): IIcon = getIcon(world.getBlockMetadata(x, y, z), kind)

  /**
   * Set of valid rotations, default is all of them
   */
  def getValidFacings: util.EnumSet[ForgeDirection] = {
    return util.EnumSet.allOf(classOf[ForgeDirection])
  }

  /**
   * Rotation to show when it's unavailable (like rendering item in inventory)
   */
  def getDefaultFacing: ForgeDirection = ForgeDirection.UP

  /**
   * Those will be overridden to provide concrete storage
   */
  def setFacing(world: World, x: Int, y: Int, z: Int, facing: ForgeDirection)
  def getFacing(world: IBlockAccess, x: Int, y: Int, z: Int): ForgeDirection

  override def rotateBlock(worldObj: World, x: Int, y: Int, z: Int, axis: ForgeDirection): Boolean = {
    if (getValidFacings.contains(axis)) {
      setFacing(worldObj, x, y, z, axis)
      return true
    }
    return false
  }

  @SideOnly(Side.CLIENT)
  override def getIcon(side: Int, meta: Int): IIcon = getIcon(meta, IconType.fromSideAndDir(side, getDefaultFacing))

  @SideOnly(Side.CLIENT)
  override def getIcon(world: IBlockAccess, x: Int, y: Int, z: Int, side: Int): IIcon = getIcon(world, x, y, z, IconType.fromSideAndDir(side, getFacing(world, x, y, z)))

  override def getValidRotations(worldObj: World, x: Int, y: Int, z: Int): Array[ForgeDirection] = getValidFacings.toArray(Array.empty[ForgeDirection])

  override def onBlockPlacedBy(world: World, x: Int, y: Int, z: Int, ent: EntityLivingBase, stack: ItemStack) {
    val dir = RotatedHelper.getFacingFromEntity(ent, getValidFacings, getDefaultFacing)
    setFacing(world, x, y, z, dir)
    super.onBlockPlacedBy(world, x, y, z, ent, stack)
  }
}