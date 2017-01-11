/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render.connected

import net.bdew.lib.block.BlockFace
import net.bdew.lib.render.QuadBakerDefault
import net.bdew.lib.render.models._
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Vec3i
import net.minecraft.util.{BlockRenderLayer, EnumFacing, ResourceLocation}
import net.minecraftforge.client.MinecraftForgeClient

class ConnectedModelEnhancer(frame: ResourceLocation) extends ModelEnhancer {
  override def additionalTextureLocations = super.additionalTextureLocations ++ List(frame)

  //why the fuck is that not part of the class?!?
  def addVec(v1: Vec3i, v2: Vec3i) = new Vec3i(v1.getX + v2.getX, v1.getY + v2.getY, v1.getZ + v2.getZ)

  override def processBlockQuads(state: IBlockState, side: EnumFacing, rand: Long, textures: Map[ResourceLocation, TextureAtlasSprite], base: () => List[BakedQuad]): List[BakedQuad] = {
    if (state != null && side != null && MinecraftForgeClient.getRenderLayer == BlockRenderLayer.CUTOUT) {
      (ConnectionsProperty.get(state) map { connections =>
        val frameSprite = textures(frame)
        val sides = BlockFace.neighbourFaces(side)

        var corners = List.empty[ConnectedModelHelper.Corner.Value]

        val U = !connections(sides.top.getDirectionVec)
        val D = !connections(sides.bottom.getDirectionVec)
        val L = !connections(sides.left.getDirectionVec)
        val R = !connections(sides.right.getDirectionVec)

        if (!U && !R && !connections(addVec(sides.top.getDirectionVec, sides.right.getDirectionVec)))
          corners :+= ConnectedModelHelper.Corner.TR

        if (!U && !L && !connections(addVec(sides.top.getDirectionVec, sides.left.getDirectionVec)))
          corners :+= ConnectedModelHelper.Corner.TL

        if (!D && !R && !connections(addVec(sides.bottom.getDirectionVec, sides.right.getDirectionVec)))
          corners :+= ConnectedModelHelper.Corner.BR

        if (!D && !L && !connections(addVec(sides.bottom.getDirectionVec, sides.left.getDirectionVec)))
          corners :+= ConnectedModelHelper.Corner.BL

        if (U) corners :+= ConnectedModelHelper.Corner.T
        if (D) corners :+= ConnectedModelHelper.Corner.B
        if (R) corners :+= ConnectedModelHelper.Corner.R
        if (L) corners :+= ConnectedModelHelper.Corner.L

        QuadBakerDefault.bakeList(corners.map(corner =>
          ConnectedModelHelper.faceQuads((corner, side))
            .withTexture(ConnectedModelHelper.faceEdges(corner).texture(frameSprite))
        ))
      }) getOrElse List.empty
    } else super.processBlockQuads(state, side, rand, textures, base)
  }

  override def processItemQuads(stack: ItemStack, side: EnumFacing, rand: Long, mode: TransformType, textures: Map[ResourceLocation, TextureAtlasSprite], base: () => List[BakedQuad]): List[BakedQuad] = {
    if (stack != null && side != null) {
      val frameSprite = textures(frame)
      super.processItemQuads(stack, side, rand, mode, textures, base) :+
        QuadBakerDefault.bakeQuad(
          ConnectedModelHelper.faceQuads((ConnectedModelHelper.Corner.ALL, side))
            .withTexture(ConnectedModelHelper.faceEdges(ConnectedModelHelper.Corner.ALL).texture(frameSprite))
        )
    } else super.processItemQuads(stack, side, rand, mode, textures, base)
  }
}
