/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.covers

import net.bdew.lib.Client
import net.bdew.lib.render.models.ModelEnhancer
import net.bdew.lib.render.primitive.{Texture, Vertex}
import net.bdew.lib.render.{Cuboid, QuadBakerDefault}
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.util.{BlockRenderLayer, EnumFacing, ResourceLocation}
import net.minecraftforge.client.MinecraftForgeClient

object CoverModelEnhancer extends ModelEnhancer {
  lazy val faceQuads = EnumFacing.values().map(f => f -> Cuboid.face(Vertex(-0.01f, -0.01f, -0.01f), Vertex(1.01f, 1.01f, 1.01f), f)).toMap
  override def processBlockQuads(state: IBlockState, side: EnumFacing, rand: Long, textures: Map[ResourceLocation, TextureAtlasSprite], base: () => List[BakedQuad]): List[BakedQuad] = {
    import scala.collection.JavaConversions._
    if (MinecraftForgeClient.getRenderLayer == BlockRenderLayer.CUTOUT) {
      for {
        faces <- CoversProperty.get(state).toList
        (face, stack) <- faces if face == side
        quad <- Client.minecraft.getRenderItem.getItemModelMesher.getItemModel(stack).getQuads(null, null, rand)
      } yield {
        QuadBakerDefault.bakeQuad(faceQuads(side).withTexture(Texture(quad.getSprite), shading = false))
      }
    } else super.processBlockQuads(state, side, rand, textures, base)
  }
}
