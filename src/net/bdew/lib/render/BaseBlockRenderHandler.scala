/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render

import cpw.mods.fml.client.registry.{ISimpleBlockRenderingHandler, RenderingRegistry}

abstract class BaseBlockRenderHandler extends ISimpleBlockRenderingHandler {
  val id = RenderingRegistry.getNextAvailableRenderId
  RenderingRegistry.registerBlockHandler(this)

  override def shouldRender3DInInventory(modelId: Int) = true
  override def getRenderId = id
}
