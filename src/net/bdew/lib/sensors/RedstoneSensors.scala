/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.sensors

import net.bdew.lib.Misc
import net.bdew.lib.gui.{DrawTarget, Rect}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

abstract class RedstoneSensors[T] extends SensorSystem[T, Boolean](false) {
  //FIXME: rendering
  //  lazy val rsOn = new IconWrapper(Texture.BLOCKS, Blocks.redstone_torch.getIcon(0, 0))
  //  lazy val rsOff = new IconWrapper(Texture.BLOCKS, Blocks.unlit_redstone_torch.getIcon(0, 0))

  @SideOnly(Side.CLIENT)
  override def drawResult(result: Boolean, rect: Rect, target: DrawTarget): Unit = {
    //    if (result)
    //      target.drawTexture(rect, rsOn)
    //    else
    //      target.drawTexture(rect, rsOff)
  }

  @SideOnly(Side.CLIENT)
  override def getLocalizedResultText(result: Boolean): String =
    if (result)
      Misc.toLocal(localizationPrefix + ".on")
    else
      Misc.toLocal(localizationPrefix + ".off")
}
