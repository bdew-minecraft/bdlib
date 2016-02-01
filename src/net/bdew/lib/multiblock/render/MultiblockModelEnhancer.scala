/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.render

import net.bdew.lib.block.BlockFace
import net.bdew.lib.multiblock.ResourceProvider
import net.bdew.lib.render.Cuboid
import net.bdew.lib.render.connected.ConnectedModelEnhancer
import net.bdew.lib.render.models.SimpleBakedModelBuilder
import net.bdew.lib.render.primitive.{Texture, Vertex}
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.util.{EnumFacing, ResourceLocation}

class MultiblockModelEnhancer(resources: ResourceProvider) extends ConnectedModelEnhancer(resources.edge) {
  override def additionalTextureLocations = super.additionalTextureLocations ++ List(resources.arrow, resources.output)

  lazy val quads =
    EnumFacing.values().map(f => f -> Cuboid.face(Vertex(-0.01f, -0.01f, -0.01f), Vertex(1.01f, 1.01f, 1.01f), f)).toMap

  override def generateBlockOverlay(builder: SimpleBakedModelBuilder, state: IBlockState, textures: Map[ResourceLocation, TextureAtlasSprite]) = {
    super.generateBlockOverlay(builder, state, textures)
    OutputFaceProperty.get(state) foreach { outputs =>
      val output = Texture(textures(resources.output))
      val arrow = Texture(textures(resources.arrow))
      for (face <- EnumFacing.values()) {
        if (outputs.isDefinedAt(face)) builder.addQuad(face, quads(face).withTexture(output, outputs(face), false))

        val neighbours = BlockFace.neighbourFaces(face)

        if (outputs.isDefinedAt(neighbours.top))
          builder.addQuad(face, quads(face).withTexture(arrow, outputs(neighbours.top), false))

        if (outputs.isDefinedAt(neighbours.right))
          builder.addQuad(face, quads(face).withTexture(arrow.rotate(1), outputs(neighbours.right), false))

        if (outputs.isDefinedAt(neighbours.bottom))
          builder.addQuad(face, quads(face).withTexture(arrow.rotate(2), outputs(neighbours.bottom), false))

        if (outputs.isDefinedAt(neighbours.left))
          builder.addQuad(face, quads(face).withTexture(arrow.rotate(3), outputs(neighbours.left), false))
      }
    }
  }
}
