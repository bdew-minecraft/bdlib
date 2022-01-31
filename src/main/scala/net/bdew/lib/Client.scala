package net.bdew.lib

import net.minecraft.client.Minecraft
import net.minecraft.client.color.block.BlockColors
import net.minecraft.client.color.item.ItemColors
import net.minecraft.client.gui.Font
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.player.LocalPlayer
import net.minecraft.client.renderer.texture.{TextureAtlas, TextureManager}
import net.minecraft.client.sounds.SoundManager
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation

/**
 * Misc functions and shortcuts for client-side code
 */
object Client {

  def minecraft: Minecraft = Minecraft.getInstance()
  def fontRenderer: Font = minecraft.font
  def textureManager: TextureManager = minecraft.textureManager
  def soundManager: SoundManager = minecraft.getSoundManager
  def world: ClientLevel = minecraft.level
  def player: LocalPlayer = minecraft.player

  def blocksAtlas: ResourceLocation = TextureAtlas.LOCATION_BLOCKS
  def blockColors: BlockColors = minecraft.getBlockColors
  def itemColors: ItemColors = minecraft.getItemColors

  def doRenderUpdate(pos: BlockPos): Unit = {
    minecraft.levelRenderer.setSectionDirty(pos.getX >> 4, pos.getY >> 4, pos.getZ >> 4)
  }
}
