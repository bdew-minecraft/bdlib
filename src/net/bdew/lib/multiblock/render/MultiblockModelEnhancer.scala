/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.render

import net.bdew.lib.block.BlockFace
import net.bdew.lib.multiblock.ResourceProvider
import net.bdew.lib.render.connected.ConnectedModelEnhancer
import net.bdew.lib.render.primitive.{Texture, Vertex}
import net.bdew.lib.render.{Cuboid, QuadBakerDefault}
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.util.{BlockRenderLayer, EnumFacing, ResourceLocation}
import net.minecraftforge.client.MinecraftForgeClient

class MultiblockModelEnhancer(resources: ResourceProvider) extends ConnectedModelEnhancer(resources.edge) {
  override def additionalTextureLocations = super.additionalTextureLocations ++ List(resources.arrow, resources.output)

  lazy val quads =
    EnumFacing.values().map(f => f -> Cuboid.face(Vertex(-0.01f, -0.01f, -0.01f), Vertex(1.01f, 1.01f, 1.01f), f)).toMap

  override def processBlockQuads(state: IBlockState, side: EnumFacing, rand: Long, textures: Map[ResourceLocation, TextureAtlasSprite], base: () => List[BakedQuad]) = {
    var list = super.processBlockQuads(state, side, rand, textures, base)
    if (state != null && side != null && MinecraftForgeClient.getRenderLayer == BlockRenderLayer.CUTOUT) {
      OutputFaceProperty.get(state) foreach { outputs =>
        val output = Texture(textures(resources.output))
        val arrow = Texture(textures(resources.arrow))

        if (outputs.isDefinedAt(side)) list :+= QuadBakerDefault.bakeQuad(quads(side).withTexture(output, outputs(side), false))

        val neighbours = BlockFace.neighbourFaces(side)

        if (outputs.isDefinedAt(neighbours.top))
          list :+= QuadBakerDefault.bakeQuad(quads(side).withTexture(arrow, outputs(neighbours.top), false))

        if (outputs.isDefinedAt(neighbours.right))
          list :+= QuadBakerDefault.bakeQuad(quads(side).withTexture(arrow.rotate(1), outputs(neighbours.right), false))

        if (outputs.isDefinedAt(neighbours.bottom))
          list :+= QuadBakerDefault.bakeQuad(quads(side).withTexture(arrow.rotate(2), outputs(neighbours.bottom), false))

        if (outputs.isDefinedAt(neighbours.left))
          list :+= QuadBakerDefault.bakeQuad(quads(side).withTexture(arrow.rotate(3), outputs(neighbours.left), false))
      }
    }
    list
  }
}
