/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render

import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.ItemMeshDefinition
import net.minecraft.client.renderer.block.model.{ModelBakery, ModelResourceLocation}
import net.minecraft.client.renderer.block.statemap.StateMapperBase
import net.minecraft.item.{Item, ItemStack}
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fluids.BlockFluidBase

object FluidModelUtils {
  /**
    * Does all the needed setup for fluid models to work. I think.
    *
    * @param block               fluid block
    * @param blockStatesLocation location of blockstates json file (including domain) - should have states named after each fluid
    */
  def registerFluidModel(block: BlockFluidBase, blockStatesLocation: String) = {
    val item = Item.getItemFromBlock(block)
    val modelResourceLocation = new ModelResourceLocation(blockStatesLocation, block.getFluid.getName)
    ModelBakery.registerItemVariants(item, modelResourceLocation)
    ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition {
      override def getModelLocation(stack: ItemStack): ModelResourceLocation = modelResourceLocation
    })
    ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
      override def getModelResourceLocation(state: IBlockState): ModelResourceLocation = modelResourceLocation
    })
  }
}
