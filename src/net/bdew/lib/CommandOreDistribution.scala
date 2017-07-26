/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib

import java.io.{BufferedWriter, File, FileWriter}

import net.bdew.lib.PimpVanilla._
import net.minecraft.command.{CommandBase, ICommandSender, WrongUsageException}
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.Item
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.fml.relauncher.FMLInjectionData
import net.minecraftforge.oredict.OreDictionary

import scala.collection.mutable

object CommandOreDistribution extends CommandBase {
  override def getName = "oredistribution"
  override def getRequiredPermissionLevel = 2
  override def getUsage(c: ICommandSender) = "bdlib.oredistribution.usage"

  def safeInt(s: String) =
    try {
      s.toInt
    } catch {
      case e: NumberFormatException => throw new WrongUsageException("bdlib.oredistribution.usage")
    }

  def wrapColor(s: String, c: TextFormatting) = c + s + TextFormatting.RESET

  override def execute(server: MinecraftServer, sender: ICommandSender, baseParams: Array[String]): Unit = {
    val (flags, params) = baseParams.partition(_.startsWith("--"))

    if (params.length < 1 || params.length > 3)
      throw new WrongUsageException(getUsage(sender))

    val (startX, startZ, world) =
      sender match {
        case p: EntityPlayerMP => (p.posX.toInt, p.posZ.toInt, p.world)
        case _ => (0, 0, sender.getEntityWorld)
      }

    val radius = safeInt(params.head)
    val minY = if (params.length >= 2) safeInt(params(1)) else 0
    val maxY = if (params.length >= 3) safeInt(params(2)) else world.getHeight - 1

    if (minY < 0 || radius <= 0 || minY > maxY || maxY > world.getHeight - 1)
      throw new WrongUsageException(getUsage(sender))

    val toFile = flags.contains("--file")

    CommandBase.notifyCommandListener(sender, this, "bdlib.oredistribution.start1",
      Array[Integer](startX - radius, minY, startZ - radius, startX + radius, maxY, startZ + radius, world.provider.getDimension): _*)

    CommandBase.notifyCommandListener(sender, this, "bdlib.oredistribution.start2")

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
      bp <- new BlockPos(startX - radius, minY, startZ - radius) to new BlockPos(startX + radius, maxY, startZ + radius)
      if !world.isAirBlock(bp)
    } {
      val state = world.getBlockState(bp)
      val stack = state.getBlock.getItem(world, bp, state)
      if (!stack.isEmpty) {
        val key = (stack.getItem, stack.getItemDamage)
        for (ore <- ores.get(key)) {
          distribution(ore) += 1
          kinds(ore) += key
        }
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
          startX - radius, minY, startZ - radius, startX + radius, maxY, startZ + radius, world.provider.getDimension
        ))
        dumpWriter.newLine()
        dumpWriter.newLine()
        if (warnings.nonEmpty) {
          for ((id, types) <- warnings.toList.sortBy(_._1)) {
            dumpWriter.write("Warning: %s has multiple variants generated:".format(id))
            dumpWriter.newLine()
            for ((item, dmg) <- types) {
              dumpWriter.write(" - %s@%s".format(item.getRegistryName, dmg))
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
        CommandBase.notifyCommandListener(sender, this, "bdlib.oredistribution.saved", dumpFile.getCanonicalPath)
      } catch {
        case e: Throwable =>
          CommandBase.notifyCommandListener(sender, this, "bdlib.oredistribution.error", e.toString)
          BdLib.logErrorException("Failed to save ore distribution dump", e)
      } finally {
        dumpWriter.close()
      }
    } else {
      // === OUTPUT TO CHAT ===
      for ((id, types) <- warnings.toList.sortBy(_._1)) {
        CommandBase.notifyCommandListener(sender, this, "bdlib.oredistribution.warn",
          wrapColor(" !", TextFormatting.RED), wrapColor(id, TextFormatting.RED))
        for ((item, dmg) <- types)
          CommandBase.notifyCommandListener(sender, this, wrapColor(" - ", TextFormatting.RED) + item.getRegistryName + "@" + dmg)
      }
      for ((id, num) <- distribution.filter(_._2 > 0).toList.sortBy(-_._2)) {
        CommandBase.notifyCommandListener(sender, this, "bdlib.oredistribution.entry",
          " *", wrapColor(id, TextFormatting.YELLOW),
          DecFormat.round(num), DecFormat.short(100F * num / total))
      }
    }
  }
}