/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib

import cpw.mods.fml.common.registry.GameData
import net.minecraft.command.{CommandBase, ICommandSender, WrongUsageException}
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.Item
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.oredict.OreDictionary

import scala.collection.mutable

object CommandOreDistribution extends CommandBase {
  def getCommandName = "oredistribution"
  override def getRequiredPermissionLevel = 2
  def getCommandUsage(c: ICommandSender) = "bdlib.oredistribution.usage"

  def safeInt(s: String) =
    try {
      s.toInt
    } catch {
      case e: NumberFormatException => throw new WrongUsageException("bdlib.oredistribution.usage")
    }

  def wrapColor(s: String, c: EnumChatFormatting) = c + s + EnumChatFormatting.RESET

  def processCommand(sender: ICommandSender, params: Array[String]) {
    if (params.length < 1 || params.length > 3)
      throw new WrongUsageException(getCommandUsage(sender))

    val (startX, startZ, world) =
      sender match {
        case p: EntityPlayerMP => (p.posX.toInt, p.posZ.toInt, p.worldObj)
        case _ => (0, 0, sender.getEntityWorld)
      }

    val radius = safeInt(params(0))
    val minY = if (params.length >= 2) safeInt(params(1)) else 0
    val maxY = if (params.length >= 3) safeInt(params(2)) else world.getHeight - 1

    if (minY < 0 || radius <= 0 || minY > maxY || maxY > world.getHeight - 1)
      throw new WrongUsageException(getCommandUsage(sender))

    // why java, WHY?
    CommandBase.func_152373_a(sender, this, "bdlib.oredistribution.start1",
      Array[Integer](startX - radius, minY, startZ - radius, startX + radius, maxY, startZ + radius, world.provider.dimensionId): _*)

    CommandBase.func_152373_a(sender, this, "bdlib.oredistribution.start2")

    import scala.collection.JavaConversions._

    val ores = (OreDictionary.getOreNames.filter(_.startsWith("ore")) flatMap { name =>
      for (s <- OreDictionary.getOres(name)) yield (s.getItem, s.getItemDamage) -> name
    }).toMap

    val distribution = mutable.Map.empty[String, Int].withDefaultValue(0)
    val kinds = mutable.Map.empty[String, Set[(Item, Int)]].withDefaultValue(Set.empty)

    for {
      x <- startX - radius to startX + radius
      y <- minY to maxY
      z <- startZ - radius to startZ + radius
      if !world.isAirBlock(x, y, z)
    } {
      val key = (Item.getItemFromBlock(world.getBlock(x, y, z)), world.getBlockMetadata(x, y, z))
      for (ore <- ores.get(key)) {
        distribution(ore) += 1
        kinds(ore) += key
      }
    }

    for ((id, types) <- kinds.filter(_._2.size > 1).toList.sortBy(_._1)) {
      CommandBase.func_152373_a(sender, this, "bdlib.oredistribution.warn",
        wrapColor(" !", EnumChatFormatting.RED), wrapColor(id.substring(3), EnumChatFormatting.RED))
      for ((item, dmg) <- types)
        CommandBase.func_152373_a(sender, this, wrapColor(" - ", EnumChatFormatting.RED) + GameData.getItemRegistry.getNameForObject(item) + "@" + dmg)
    }

    val total = distribution.map(_._2).sum

    for ((id, num) <- distribution.filter(_._2 > 0).toList.sortBy(-_._2)) {
      CommandBase.func_152373_a(sender, this, "bdlib.oredistribution.entry",
        " *", wrapColor(id.substring(3), EnumChatFormatting.YELLOW),
        DecFormat.round(num), DecFormat.short(100F * num / total))
    }
  }
}