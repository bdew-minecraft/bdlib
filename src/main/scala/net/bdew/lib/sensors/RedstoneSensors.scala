package net.bdew.lib.sensors

import com.mojang.blaze3d.vertex.PoseStack
import net.bdew.lib.Text
import net.bdew.lib.gui.{DrawTarget, Rect, Texture}
import net.minecraft.network.chat.Component
import net.minecraftforge.api.distmarker.{Dist, OnlyIn}

abstract class RedstoneSensors[T] extends SensorSystem[T, Boolean](false) {
  private lazy val rsOn = Texture.block("minecraft:block/redstone_torch")
  private lazy val rsOff = Texture.block("minecraft:block/redstone_torch_off")

  @OnlyIn(Dist.CLIENT)
  override def drawResult(m: PoseStack, result: Boolean, rect: Rect, target: DrawTarget): Unit = {
    if (result)
      target.drawTexture(m, rect, rsOn)
    else
      target.drawTexture(m, rect, rsOff)
  }

  @OnlyIn(Dist.CLIENT)
  override def getResultText(result: Boolean): Component =
    if (result)
      Text.translate(localizationPrefix + ".on")
    else
      Text.translate(localizationPrefix + ".off")
}
