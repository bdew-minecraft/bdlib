/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.sensors

import com.mojang.blaze3d.matrix.MatrixStack
import net.bdew.lib.Text
import net.bdew.lib.gui.{DrawTarget, Rect, Texture}
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.api.distmarker.{Dist, OnlyIn}

abstract class RedstoneSensors[T] extends SensorSystem[T, Boolean](false) {
  private lazy val rsOn = Texture.block("minecraft:block/redstone_torch")
  private lazy val rsOff = Texture.block("minecraft:block/redstone_torch_off")

  @OnlyIn(Dist.CLIENT)
  override def drawResult(m: MatrixStack, result: Boolean, rect: Rect, target: DrawTarget): Unit = {
    if (result)
      target.drawTexture(m, rect, rsOn)
    else
      target.drawTexture(m, rect, rsOff)
  }

  @OnlyIn(Dist.CLIENT)
  override def getResultText(result: Boolean): ITextComponent =
    if (result)
      Text.translate(localizationPrefix + ".on")
    else
      Text.translate(localizationPrefix + ".off")
}
