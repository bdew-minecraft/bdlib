package net.bdew.lib.render.connected

import net.bdew.lib.PimpVanilla.pimpModelData
import net.bdew.lib.block.BlockFace
import net.bdew.lib.render.QuadBakerDefault
import net.bdew.lib.render.models._
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.core.{BlockPos, Direction, Vec3i}
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockAndTintGetter
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.client.MinecraftForgeClient
import net.minecraftforge.client.model.data.IModelData

import scala.util.Random

class ConnectedModelEnhancer(frame: ResourceLocation) extends ModelEnhancer {
  override def additionalTextureLocations: List[ResourceLocation] = super.additionalTextureLocations ++ List(frame)

  override def processBlockQuads(state: BlockState, side: Direction, rand: Random, data: IModelData, textures: Map[ResourceLocation, TextureAtlasSprite], base: () => List[BakedQuad]): List[BakedQuad] = {
    if (state != null && side != null && MinecraftForgeClient.getRenderType == RenderType.cutout()) {
      (data.getDataOpt(ConnectionsProperty) map { connections =>
        val frameSprite = textures(frame)
        val sides = BlockFace.neighbourFaces(side)

        var corners = List.empty[ConnectedModelHelper.Corner.Value]

        val U = !connections(sides.top.getNormal)
        val D = !connections(sides.bottom.getNormal)
        val L = !connections(sides.left.getNormal)
        val R = !connections(sides.right.getNormal)

        if (!U && !R && !connections(sides.top.getNormal.offset(sides.right.getNormal)))
          corners :+= ConnectedModelHelper.Corner.TR

        if (!U && !L && !connections(sides.top.getNormal.offset(sides.left.getNormal)))
          corners :+= ConnectedModelHelper.Corner.TL

        if (!D && !R && !connections(sides.bottom.getNormal.offset(sides.right.getNormal)))
          corners :+= ConnectedModelHelper.Corner.BR

        if (!D && !L && !connections(sides.bottom.getNormal.offset(sides.left.getNormal)))
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
    base && MinecraftForgeClient.getRenderType != RenderType.cutout()
  }

  override def extendModelData(world: BlockAndTintGetter, pos: BlockPos, state: BlockState, base: IModelData): IModelData = {
    if (!state.getBlock.isInstanceOf[ConnectedTextureBlock]) return base
    val block = state.getBlock.asInstanceOf[ConnectedTextureBlock]

    val connections = for (x <- -1 to +1; y <- -1 to +1; z <- -1 to +1 if x != 0 || y != 0 || z != 0) yield {
      val vector = new Vec3i(x, y, z)
      vector -> block.canConnect(world, pos, pos.offset(vector))
    }

    base.withData(ConnectionsProperty, connections.toMap)
  }
}
