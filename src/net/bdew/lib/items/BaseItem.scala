/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.items

import net.bdew.lib.{BdLib, Client, Misc}
import net.minecraft.block.Block
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.{Item, ItemBlock, ItemTool}
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

trait BaseItemMixin extends Item {
  val name: String
  val modId = Misc.getActiveModId

  setUnlocalizedName(modId + "." + name)
  setRegistryName(modId, name)

  /**
    * Registers item model. Called from ItemManager after inserting into registry
    * Override to provide custom models.
    */
  @SideOnly(Side.CLIENT)
  def registerItemModels(): Unit = {
    ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName, "inventory"))
    // If this is called late (after preinit) register manually with ItemModelMesher - as forge won't do it
    if (Client.minecraft.getRenderItem != null) {
      BdLib.logDebug("Registering late item model for " + getRegistryName)
      Client.minecraft.getRenderItem.getItemModelMesher.register(this, 0, new ModelResourceLocation(getRegistryName, "inventory"))
    }
  }
}

import scala.collection.JavaConverters._

class BaseItem(val name: String) extends Item with BaseItemMixin

class BaseTool(val name: String, material: Item.ToolMaterial, effectiveBlocks: Block*) extends ItemTool(material, effectiveBlocks.toSet.asJava) with BaseItemMixin

class BaseItemBlock(val name: String, block: Block) extends ItemBlock(block) with BaseItemMixin