package net.bdew.lib

import net.minecraft.client.Minecraft
import net.minecraft.client.audio.SoundHandler
import net.minecraft.client.entity.player.ClientPlayerEntity
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.color.{BlockColors, ItemColors}
import net.minecraft.client.renderer.texture.{AtlasTexture, TextureManager}
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos

/**
 * Misc functions and shortcuts for client-side code
 */
object Client {

  def minecraft: Minecraft = Minecraft.getInstance()
  def fontRenderer: FontRenderer = minecraft.font
  def textureManager: TextureManager = minecraft.textureManager
  def soundManager: SoundHandler = minecraft.getSoundManager
  def world: ClientWorld = minecraft.level
  def player: ClientPlayerEntity = minecraft.player

  def blocksAtlas: ResourceLocation = AtlasTexture.LOCATION_BLOCKS
  def blockColors: BlockColors = minecraft.getBlockColors
  def itemColors: ItemColors = minecraft.getItemColors

  def doRenderUpdate(pos: BlockPos): Unit = {
    minecraft.levelRenderer.setSectionDirty(pos.getX >> 4, pos.getY >> 4, pos.getZ >> 4)
  }
}
