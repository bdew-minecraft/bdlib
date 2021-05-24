/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render.connected

import net.bdew.lib.PimpVanilla.pimpModelData
import net.bdew.lib.block.BlockFace
import net.bdew.lib.render.QuadBakerDefault
import net.bdew.lib.render.models._
import net.minecraft.block.BlockState
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.model.BakedQuad
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.vector.Vector3i
import net.minecraft.util.{Direction, ResourceLocation}
import net.minecraft.world.IBlockDisplayReader
import net.minecraftforge.client.MinecraftForgeClient
import net.minecraftforge.client.model.data.IModelData

import scala.util.Random

class ConnectedModelEnhancer(frame: ResourceLocation) extends ModelEnhancer {
  override def additionalTextureLocations: List[ResourceLocation] = super.additionalTextureLocations ++ List(frame)

  //why the fuck is that not part of the class?!?
  def addVec(v1: Vector3i, v2: Vector3i) = new Vector3i(v1.getX + v2.getX, v1.getY + v2.getY, v1.getZ + v2.getZ)

  override def processBlockQuads(state: BlockState, side: Direction, rand: Random, data: IModelData, textures: Map[ResourceLocation, TextureAtlasSprite], base: () => List[BakedQuad]): List[BakedQuad] = {
    if (state != null && side != null && MinecraftForgeClient.getRenderLayer == RenderType.cutout()) {
      (data.getDataOpt(ConnectionsProperty) map { connections =>
        val frameSprite = textures(frame)
        val sides = BlockFace.neighbourFaces(side)

        var corners = List.empty[ConnectedModelHelper.Corner.Value]

        val U = !connections(sides.top.getNormal)
        val D = !connections(sides.bottom.getNormal)
        val L = !connections(sides.left.getNormal)
        val R = !connections(sides.right.getNormal)

        if (!U && !R && !connections(addVec(sides.top.getNormal, sides.right.getNormal)))
          corners :+= ConnectedModelHelper.Corner.TR

        if (!U && !L && !connections(addVec(sides.top.getNormal, sides.left.getNormal)))
          corners :+= ConnectedModelHelper.Corner.TL

        if (!D && !R && !connections(addVec(sides.bottom.getNormal, sides.right.getNormal)))
          corners :+= ConnectedModelHelper.Corner.BR

        if (!D && !L && !connections(addVec(sides.bottom.getNormal, sides.left.getNormal)))
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
    } else super.processBlockQuads(state, side, rand, data, textures, base)
  }

  override def processItemQuads(stack: ItemStack, side: Direction, rand: Random, mode: TransformType, textures: Map[ResourceLocation, TextureAtlasSprite], base: () => List[BakedQuad]): List[BakedQuad] = {
    if (stack != null && side != null) {
      val frameSprite = textures(frame)
      super.processItemQuads(stack, side, rand, mode, textures, base) :+
        QuadBakerDefault.bakeQuad(
          ConnectedModelHelper.faceQuads((ConnectedModelHelper.Corner.ALL, side))
            .withTexture(ConnectedModelHelper.faceEdges(ConnectedModelHelper.Corner.ALL).texture(frameSprite))
        )
    } else super.processItemQuads(stack, side, rand, mode, textures, base)
  }


  override def overrideAmbientOcclusion(base: Boolean): Boolean = {
    base && MinecraftForgeClient.getRenderLayer != RenderType.cutout()
  }

  override def extendModelData(world: IBlockDisplayReader, pos: BlockPos, state: BlockState, base: IModelData): IModelData = {
    if (!state.getBlock.isInstanceOf[ConnectedTextureBlock]) return base
    val block = state.getBlock.asInstanceOf[ConnectedTextureBlock]

    val connections = for (x <- -1 to +1; y <- -1 to +1; z <- -1 to +1 if x != 0 || y != 0 || z != 0) yield {
      val vector = new Vector3i(x, y, z)
      vector -> block.canConnect(world, pos, pos.offset(vector))
    }

    base.withData(ConnectionsProperty, connections.toMap)
  }
}
