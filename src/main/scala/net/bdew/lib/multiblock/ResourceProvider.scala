package net.bdew.lib.multiblock

import net.bdew.lib.gui.{Color, Texture}
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.IFormattableTextComponent

trait ResourceProvider {
  // Icons for rendering
  def edge: ResourceLocation
  def output: ResourceLocation
  def arrow: ResourceLocation
  def disabled: ResourceLocation

  // Textures for GUI stuff
  def btBase: Texture
  def btHover: Texture
  def btRsOn: Texture
  def btRsOff: Texture
  def btEnabled: Texture
  def btDisabled: Texture

  val outputColors: Map[Int, Color]
  val unlocalizedOutputName: Map[Int, String]
  def getModuleName(s: String): IFormattableTextComponent
  def getMachineName(s: String): IFormattableTextComponent
}
