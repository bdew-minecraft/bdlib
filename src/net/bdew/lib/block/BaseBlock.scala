/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.block

import net.bdew.lib.{BdLib, Client, Misc}
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.{BlockStateContainer, IBlockState}
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.Item
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.common.property.{ExtendedBlockState, IUnlistedProperty}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

import scala.language.existentials

trait BaseBlockMixin extends Block {
  val name: String
  val modId = Misc.getActiveModId
  setRegistryName(modId, name)
  setUnlocalizedName(modId + "." + name)

  // fuck java, fuck mojang, fuck everything!
  def getProperties = List.empty[IProperty[T] forSome {type T <: Comparable[T]}]
  def getUnlistedProperties = List.empty[IUnlistedProperty[_]]

  final override def createBlockState(): BlockStateContainer = {
    val normal = getProperties
    val unlisted = getUnlistedProperties
    if (unlisted.isEmpty) {
      new BlockStateContainer(this, normal.toSeq: _*)
    } else {
      new ExtendedBlockState(this, normal.toArray, unlisted.toArray)
    }
  }

  /**
    * Non-protected version of setDefaultState to use in mixins
    */
  def setDefaultBlockState(bs: IBlockState) = super.setDefaultState(bs)

  /**
    * Registers item model. Called from BlockManager or Machine after inserting into registry
    * Override to provide custom models.
    */
  @SideOnly(Side.CLIENT)
  def registerItemModels(): Unit = {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName, "inventory"))
    // If this is called late (after preinit) register manually with ItemModelMesher - as forge won't do it
    if (Client.minecraft.getRenderItem != null) {
      BdLib.logDebug("Registering late item model for " + getRegistryName)
      Client.minecraft.getRenderItem.getItemModelMesher.register(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName, "inventory"))
    }
  }
}

class BaseBlock(val name: String, material: Material) extends Block(material) with BaseBlockMixin
