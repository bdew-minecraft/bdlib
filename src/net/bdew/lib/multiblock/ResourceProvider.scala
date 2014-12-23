/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock

import net.bdew.lib.gui.{Color, Texture}
import net.minecraft.util.IIcon

trait ResourceProvider {
  // Icons for rendering, *must* be on block sprite sheet
  def edge: IIcon
  def disabled: IIcon
  def output: IIcon
  def arrowTop: IIcon
  def arrowRight: IIcon
  def arrowBottom: IIcon
  def arrowLeft: IIcon

  // Textures for GUI stuff
  def btBase: Texture
  def btHover: Texture
  def btRsOn: Texture
  def btRsOff: Texture
  def btEnabled: Texture
  def btDisabled: Texture

  val outputColors: Map[Int, Color]
  val unlocalizedOutputName: Map[Int, String]
  def getModuleName(s: String): String
}
