/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.player

import net.bdew.lib.BdLib
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent._

import scala.collection.mutable

class PlayerCache[T] extends mutable.Map[EntityPlayer, T] {
  var map = Map.empty[EntityPlayer, T]

  BdLib.onServerStarting.listen(x => map = map.empty)
  BdLib.onServerStopping.listen(x => map = map.empty)

  MinecraftForge.EVENT_BUS.register(this)

  def updateIfNeeded(p: EntityPlayer, v: T): Boolean = {
    if (map.get(p).contains(v)) {
      false
    } else {
      map += p -> v
      true
    }
  }

  def reset(p: EntityPlayer) {
    if (map.contains(p)) map -= p
  }

  override def get(p: EntityPlayer) = map.get(p)

  override def +=(kv: (EntityPlayer, T)) = {
    map += kv
    this
  }
  override def -=(key: EntityPlayer) = {
    map -= key
    this
  }

  override def iterator = map.iterator

  @SubscribeEvent
  def handlePlayerLogout(ev: PlayerLoggedOutEvent) = reset(ev.player)

  @SubscribeEvent
  def handlePlayerChangedDimension(ev: PlayerChangedDimensionEvent) = reset(ev.player)

  @SubscribeEvent
  def handlePlayerRespawn(ev: PlayerRespawnEvent) = reset(ev.player)
}
