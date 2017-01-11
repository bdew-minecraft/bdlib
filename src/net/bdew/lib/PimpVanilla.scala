/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib

import net.bdew.lib.nbt.converters._
import net.bdew.lib.rich._
import net.minecraft.block.state.IBlockState
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.BlockPos
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.common.property.IExtendedBlockState

import scala.language.implicitConversions

/**
  * This object provides a bunch of implicits that makes minecraft classes nicer to work with
  * It also includes definitions that makes working with them in NBT data easier
  */
object PimpVanilla {
  implicit def pimpBlockPos(p: BlockPos): RichBlockPos = new RichBlockPos(p)
  implicit def pimpWorld(p: World): RichWorld = new RichWorld(p)
  implicit def pimpBlockAccess(p: IBlockAccess): RichBlockAccess = new RichBlockAccess(p)
  implicit def pimpNBT(p: NBTTagCompound): RichNBTTagCompound = new RichNBTTagCompound(p)
  implicit def pimpIBlockSTate(p: IBlockState): RichBlockState = new RichBlockState(p)
  implicit def pimpIExBlockSTate(p: IExtendedBlockState): RichExtendedBlockState = new RichExtendedBlockState(p)

  // Helpers for NBT stuff

  implicit val tBlockPos = TBlockPos
  implicit val tBoolean = TBoolean
  implicit val tFluid = TFluid
  implicit val tFluidStack = TFluidStack
  implicit val tItemStack = TItemStack
  implicit val tEnumFacing = TEnumFacing
}
