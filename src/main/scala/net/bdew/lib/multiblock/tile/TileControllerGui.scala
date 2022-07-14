package net.bdew.lib.multiblock.tile

import net.bdew.lib.Text._
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Player
import net.minecraftforge.network.NetworkHooks

trait TileControllerGui extends TileController with MenuProvider {
  def onClick(player: Player): Unit = {
    val missing = cfg.required().filter({ case (mod, cnt) => getNumOfModules(mod) < cnt })
    if (missing.nonEmpty) {
      player.sendSystemMessage(translate("bdlib.multiblock.incomplete").setColor(Color.RED))
      for ((mod, cnt) <- missing)
        player.sendSystemMessage(translate("bdlib.multiblock.incomplete.entry", cnt.toString, resources.getModuleName(mod.id).setColor(Color.RED)))
    } else player match {
      case serverPlayer: ServerPlayer =>
        NetworkHooks.openScreen(serverPlayer, this, getBlockPos)
      case _ => // nothing
    }
  }
}
