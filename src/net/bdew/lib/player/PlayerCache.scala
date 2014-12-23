/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.player

import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.PlayerEvent._
import net.bdew.lib.BdLib
import net.minecraft.entity.player.EntityPlayer

import scala.collection.mutable

class PlayerCache[T] extends mutable.Map[EntityPlayer, T] {
  var map = Map.empty[EntityPlayer, T]

  BdLib.onServerStarting.listen(x => map = map.empty)
  BdLib.onServerStopping.listen(x => map = map.empty)
  FMLCommonHandler.instance().bus().register(this)

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
