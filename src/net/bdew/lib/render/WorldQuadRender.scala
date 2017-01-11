/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render

import net.bdew.lib.render.primitive.TQuad
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraftforge.client.model.pipeline.VertexBufferConsumer
import org.lwjgl.opengl.GL11

object WorldQuadRender {
  def renderQuads(quads: Traversable[TQuad]): Unit = {
    val T = Tessellator.getInstance()
    val B = T.getBuffer
    B.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)
    for (quad <- quads; vertex <- quad.vertexes)
      B.pos(vertex.x, vertex.y, vertex.z).tex(vertex.u, vertex.v).endVertex()
    T.draw()
  }

  def renderBakedQuads(quads: Traversable[BakedQuad]): Unit = {
    val T = Tessellator.getInstance()
    val B = T.getBuffer
    B.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)
    val consumer = new VertexBufferConsumer(B)
    for (quad <- quads)
      quad.pipe(consumer)
    T.draw()
  }
}
