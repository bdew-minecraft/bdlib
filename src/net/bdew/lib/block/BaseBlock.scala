/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.block

import net.bdew.lib.Misc
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.{BlockState, IBlockState}
import net.minecraftforge.common.property.{ExtendedBlockState, IUnlistedProperty}

import scala.language.existentials

class BaseBlock(val name: String, material: Material) extends Block(material) {
  val modId = Misc.getActiveModId
  setRegistryName(modId, name)
  setUnlocalizedName(modId + "." + name)

  // fuck java, fuck mojang, fuck everything!
  def getProperties = List.empty[IProperty[T] forSome {type T <: Comparable[T]}]
  def getUnlistedProperties = List.empty[IUnlistedProperty[_]]

  final override def createBlockState(): BlockState = {
    val normal = getProperties
    val unlisted = getUnlistedProperties
    if (unlisted.isEmpty) {
      new BlockState(this, normal.toSeq: _*)
    } else {
      new ExtendedBlockState(this, normal.toArray, unlisted.toArray)
    }
  }

  /**
    * Called before a block is registered to do any late setup
    */
  def preRegistration(): Unit = {}

  def setDefaultBlockState(bs: IBlockState) = super.setDefaultState(bs)
}
