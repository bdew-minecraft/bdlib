/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render.models

import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.util.{EnumWorldBlockLayer, ResourceLocation}
import net.minecraftforge.client.MinecraftForgeClient
import net.minecraftforge.client.model.IPerspectiveAwareModel

abstract class OverlayModelEnhancer(layer: EnumWorldBlockLayer) extends ModelEnhancer {
  def generateBlockOverlay(builder: SimpleBakedModelBuilder, state: IBlockState, textures: Map[ResourceLocation, TextureAtlasSprite])

  override def handleBlockState(base: IPerspectiveAwareModel, state: IBlockState, textures: Map[ResourceLocation, TextureAtlasSprite]) = {
    if (MinecraftForgeClient.getRenderLayer == EnumWorldBlockLayer.CUTOUT) {
      val builder = new SimpleBakedModelBuilder(base.getFormat)
      builder.inheritCameraTransformsFrom(base)
      generateBlockOverlay(builder, state, textures)
      builder.build()
    } else base
  }
}
