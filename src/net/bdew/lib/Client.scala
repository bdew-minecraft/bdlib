/*
 * Copyright (c) bdew, 2013 - 2017
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
  def fontRenderer = Minecraft.getMinecraft.fontRenderer
  def renderEngine = Minecraft.getMinecraft.renderEngine
  def world = Minecraft.getMinecraft.world
  def player = Minecraft.getMinecraft.player
  def textureManager = Minecraft.getMinecraft.getTextureManager
  def textureMapBlocks = Minecraft.getMinecraft.getTextureMapBlocks
  def blockColors = Minecraft.getMinecraft.getBlockColors
  def itemColors = Minecraft.getMinecraft.getItemColors

  def missingIcon = textureMapBlocks.getAtlasSprite("missingno")

  def shiftDown: Boolean = Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54)
}
