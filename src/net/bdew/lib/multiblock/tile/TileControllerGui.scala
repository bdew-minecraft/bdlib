/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.tile

import net.minecraft.entity.player.EntityPlayer

abstract class TileControllerGui extends TileController {
  def openGui(player: EntityPlayer)

  def onClick(player: EntityPlayer) = {
    import net.bdew.lib.helpers.ChatHelper._
    val missing = cfg.required.filter({ case (mod, cnt) => getNumOfModules(mod) < cnt })
    if (missing.nonEmpty) {
      player.addChatMessage(L("bdlib.multiblock.incomplete").setColor(Color.RED))
      for ((mod, cnt) <- missing)
        player.addChatMessage(L("- %s %s", cnt.toString, L(resources.getModuleName(mod))).setColor(Color.RED))
    } else openGui(player)
  }

}
