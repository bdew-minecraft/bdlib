/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.block

import net.bdew.lib.Misc
import net.bdew.lib.block.{BaseBlock, BlockTooltip, HasTE}
import net.bdew.lib.multiblock.tile.TileController
import net.bdew.lib.multiblock.{MachineCore, ResourceProvider}
import net.bdew.lib.render.connected.ConnectedTextureBlock
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextFormatting
import net.minecraft.util.{EnumFacing, EnumHand}
import net.minecraft.world.{IBlockAccess, World}

abstract class BlockController[T <: TileController](name: String, material: Material, val TEClass: Class[T])
  extends BaseBlock(name, material) with HasTE[T] with ConnectedTextureBlock with BlockTooltip {

  var machine: MachineCore = _

  def resources: ResourceProvider

  override def breakBlock(world: World, pos: BlockPos, state: IBlockState) = {
    getTE(world, pos).onBreak()
    super.breakBlock(world, pos, state)
  }

  override def canConnect(world: IBlockAccess, origin: BlockPos, target: BlockPos) =
    getTE(world, origin).exists(_.modules.contains(target))

  override def onBlockActivated(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer, hand: EnumHand, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean = {
    if (player.isSneaking) return false
    if (world.isRemote) return true
    getTE(world, pos).onClick(player)
    return true
  }

  override def getTooltip(stack: ItemStack, world: World, flags: ITooltipFlag): List[String] = {
    List(Misc.toLocal("bdlib.multiblock.tip.controller")) ++ (
      for ((kind, max) <- machine.modules) yield {
        val name = Misc.toLocalF(resources.getModuleName(kind))
        if (machine.required.getOrElse(kind, 0) > 0) {
          Misc.toLocalF("bdlib.multiblock.tip.module.range",
            TextFormatting.YELLOW + name + TextFormatting.RESET,
            TextFormatting.YELLOW + machine.required(kind).toString + TextFormatting.RESET,
            TextFormatting.YELLOW + max.toString + TextFormatting.RESET)
        } else {
          Misc.toLocalF("bdlib.multiblock.tip.module.max",
            TextFormatting.YELLOW + name + TextFormatting.RESET,
            TextFormatting.YELLOW + max.toString + TextFormatting.RESET)
        }
      })
  }
}


