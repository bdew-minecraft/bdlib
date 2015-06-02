/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib

import java.io.{BufferedWriter, File, FileWriter}

import cpw.mods.fml.common.registry.GameData
import cpw.mods.fml.relauncher.FMLInjectionData
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

  def processCommand(sender: ICommandSender, baseParams: Array[String]) {
    val (flags, params) = baseParams.partition(_.startsWith("--"))

    if (params.length < 1 || params.length > 3)
      throw new WrongUsageException(getCommandUsage(sender))

    val (startX, startZ, world) =
      sender match {
        case p: EntityPlayerMP => (p.posX.toInt, p.posZ.toInt, p.worldObj)
        case _ => (0, 0, sender.getEntityWorld)
      }

    val radius = safeInt(params.head)
    val minY = if (params.length >= 2) safeInt(params(1)) else 0
    val maxY = if (params.length >= 3) safeInt(params(2)) else world.getHeight - 1

    if (minY < 0 || radius <= 0 || minY > maxY || maxY > world.getHeight - 1)
      throw new WrongUsageException(getCommandUsage(sender))

    val toFile = flags.contains("--file")

    // why java, WHY?
    CommandBase.func_152373_a(sender, this, "bdlib.oredistribution.start1",
      Array[Integer](startX - radius, minY, startZ - radius, startX + radius, maxY, startZ + radius, world.provider.dimensionId): _*)

    CommandBase.func_152373_a(sender, this, "bdlib.oredistribution.start2")

    import scala.collection.JavaConversions._

    val ores = (OreDictionary.getOreNames.filter(x => x.startsWith("ore") || x.startsWith("denseore")) flatMap { name =>
      val fixedName =
        if (name.startsWith("ore"))
          name.substring(3)
        else if (name.startsWith("denseore"))
          "Dense " + name.substring(8)
        else name
      for (s <- OreDictionary.getOres(name)) yield (s.getItem, s.getItemDamage) -> fixedName
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

    val total = distribution.values.sum
    val warnings = kinds.filter(_._2.size > 1)

    if (toFile) {
      // === OUTPUT TO FILE ===
      val mcHome = FMLInjectionData.data()(6).asInstanceOf[File] //is there a better way to get this?
      val dumpFile = new File(mcHome, "ore_distribution.txt")
      val dumpWriter = new BufferedWriter(new FileWriter(dumpFile))
      try {
        dumpWriter.write("Ore distribution report - (%d,%d,%d) to (%d,%d,%d) dim %d".format(
          startX - radius, minY, startZ - radius, startX + radius, maxY, startZ + radius, world.provider.dimensionId
        ))
        dumpWriter.newLine()
        dumpWriter.newLine()
        if (warnings.nonEmpty) {
          for ((id, types) <- warnings.toList.sortBy(_._1)) {
            dumpWriter.write("Warning: %s has multiple variants generated:".format(id))
            dumpWriter.newLine()
            for ((item, dmg) <- types) {
              dumpWriter.write(" - %s@%s".format(GameData.getItemRegistry.getNameForObject(item), dmg))
              dumpWriter.newLine()
            }
            dumpWriter.newLine()
          }
          dumpWriter.write("=================")
          dumpWriter.newLine()
          dumpWriter.newLine()
        }
        for ((id, num) <- distribution.filter(_._2 > 0).toList.sortBy(-_._2)) {
          dumpWriter.write("%s - %s (%s%%)".format(id, DecFormat.round(num), DecFormat.short(100F * num / total)))
          dumpWriter.newLine()
        }
        CommandBase.func_152373_a(sender, this, "bdlib.oredistribution.saved", dumpFile.getCanonicalPath)
      } catch {
        case e: Throwable =>
          CommandBase.func_152373_a(sender, this, "bdlib.oredistribution.error", e.toString)
          BdLib.logErrorException("Failed to save ore distribution dump", e)
      } finally {
        dumpWriter.close()
      }
    } else {
      // === OUTPUT TO CHAT ===
      for ((id, types) <- warnings.toList.sortBy(_._1)) {
        CommandBase.func_152373_a(sender, this, "bdlib.oredistribution.warn",
          wrapColor(" !", EnumChatFormatting.RED), wrapColor(id, EnumChatFormatting.RED))
        for ((item, dmg) <- types)
          CommandBase.func_152373_a(sender, this, wrapColor(" - ", EnumChatFormatting.RED) + GameData.getItemRegistry.getNameForObject(item) + "@" + dmg)
      }
      for ((id, num) <- distribution.filter(_._2 > 0).toList.sortBy(-_._2)) {
        CommandBase.func_152373_a(sender, this, "bdlib.oredistribution.entry",
          " *", wrapColor(id, EnumChatFormatting.YELLOW),
          DecFormat.round(num), DecFormat.short(100F * num / total))
      }
    }
  }
}