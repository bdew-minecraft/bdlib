package net.bdew.lib.multiblock.tile

import net.bdew.lib.Text._
import net.minecraft.entity.player.{PlayerEntity, ServerPlayerEntity}
import net.minecraft.inventory.container.INamedContainerProvider
import net.minecraft.util.Util
import net.minecraftforge.fml.network.NetworkHooks

trait TileControllerGui extends TileController with INamedContainerProvider {
  def onClick(player: PlayerEntity): Unit = {
    val missing = cfg.required().filter({ case (mod, cnt) => getNumOfModules(mod) < cnt })
    if (missing.nonEmpty) {
      player.sendMessage(translate("bdlib.multiblock.incomplete").setColor(Color.RED), Util.NIL_UUID)
      for ((mod, cnt) <- missing)
        player.sendMessage(translate("bdlib.multiblock.incomplete.entry", cnt.toString, resources.getModuleName(mod.id).setColor(Color.RED)), Util.NIL_UUID)
    } else player match {
      case serverPlayer: ServerPlayerEntity =>
        NetworkHooks.openGui(serverPlayer, this, getBlockPos)
      case _ => // nothing
    }
  }
}
