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
import net.minecraft.block.BlockState
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.math.BlockPos
import net.minecraft.world.{IBlockReader, World}
import net.minecraftforge.client.model.data.IModelData

import scala.language.implicitConversions

/**
 * This object provides a bunch of implicits that makes minecraft classes nicer to work with
 * It also includes definitions that makes working with them in NBT data easier
 */
object PimpVanilla {
  implicit def pimpBlockPos(p: BlockPos): RichBlockPos = new RichBlockPos(p)
  implicit def pimpWorld(p: World): RichWorld = new RichWorld(p)
  implicit def pimpBlockReader(p: IBlockReader): RichBlockReader = new RichBlockReader(p)
  implicit def pimpNBT(p: CompoundNBT): RichCompoundNBT = new RichCompoundNBT(p)
  implicit def pimpBlockState(p: BlockState): RichBlockState = new RichBlockState(p)
  implicit def pimpModelData(p: IModelData): RichIModelData = new RichIModelData(p)

  // Helpers for NBT stuff

  implicit val tBlockPos: TBlockPos.type = TBlockPos
  implicit val tBoolean: TBoolean.type = TBoolean
  implicit val tFluid: TFluid.type = TFluid
  implicit val tFluidStack: TFluidStack.type = TFluidStack
  implicit val tItemStack: TItemStack.type = TItemStack
  implicit val tDirection: TDirection.type = TDirection
}
