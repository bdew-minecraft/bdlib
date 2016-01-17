/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib

import net.minecraft.client.Minecraft
import org.lwjgl.input.Keyboard

/**
 * Misc functions and shortcuts for client-side code
 */
object Client {
  def minecraft = Minecraft.getMinecraft
  def fontRenderer = Minecraft.getMinecraft.fontRendererObj
  def renderEngine = Minecraft.getMinecraft.renderEngine
  def world = Minecraft.getMinecraft.theWorld
  def player = Minecraft.getMinecraft.thePlayer
  def textureManager = Minecraft.getMinecraft.getTextureManager
  def textureMapBlocks = Minecraft.getMinecraft.getTextureMapBlocks
  def missingIcon = textureMapBlocks.getAtlasSprite("missingno")

  def shiftDown: Boolean = {
    return Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54)
  }

}
