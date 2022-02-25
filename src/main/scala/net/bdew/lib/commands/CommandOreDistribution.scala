package net.bdew.lib.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.bdew.lib.PimpVanilla.pimpBlockPos
import net.bdew.lib.{BdLib, DecFormat, Misc, Text}
import net.minecraft.ChatFormatting
import net.minecraft.commands.{CommandSourceStack, Commands}
import net.minecraft.core.BlockPos
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block
import net.minecraftforge.fml.loading.FMLPaths

import java.io.{BufferedWriter, FileWriter}
import scala.collection.mutable
import scala.jdk.CollectionConverters._
import scala.util.Using

object CommandOreDistribution extends ModCommand {
  private val radius = intArg("radius", min = 1)
  private val minHeight = intArg("minHeight")
  private val maxHeight = intArg("maxHeight")

  override def register: LiteralArgumentBuilder[CommandSourceStack] =
    literal("oredistribution")
      .requires(cs => cs.hasPermission(2))
      .`then`(radius.arg
        .`then`(Commands.literal("--file")
          .executes(cs => execute(cs, radius.get(cs), 0, 255, true))
        )
        .`then`(minHeight.arg
          .`then`(maxHeight.arg
            .`then`(literal("--file")
              .executes(cs => execute(cs, radius.get(cs), minHeight.get(cs), maxHeight.get(cs), true))
            ).executes(cs => execute(cs, radius.get(cs), minHeight.get(cs), maxHeight.get(cs), false))
          ).executes(cs => execute(cs, radius.get(cs), minHeight.get(cs), Int.MaxValue, false))
        ).executes(cs => execute(cs, radius.get(cs), Int.MinValue, Int.MaxValue, false))
      ).executes(sendUsage)

  def sendUsage(ctx: CommandContext[CommandSourceStack]): Int = {
    ctx.getSource.sendFailure(Text.translate("bdlib.oredistribution.usage"))
    0
  }

  def execute(ctx: CommandContext[CommandSourceStack], radius: Int, minYparam: Int, maxYparam: Int, toFile: Boolean): Int = {
    val (startX, startZ) =
      if (ctx.getSource.getEntity != null)
        (ctx.getSource.getEntity.blockPosition().getX, ctx.getSource.getEntity.blockPosition().getZ)
      else
        (0, 0)

    val world = ctx.getSource.getLevel


    val minY = Misc.max(minYparam, world.dimensionType().minY())
    val maxY = Misc.min(maxYparam, world.getHeight() + world.dimensionType().minY() - 1)

    if (radius <= 0 || minY > maxY)
      return sendUsage(ctx)

    ctx.getSource.sendSuccess(Text.translate("bdlib.oredistribution.start1",
      Integer.valueOf(startX - radius), Integer.valueOf(minY), Integer.valueOf(startZ - radius),
      Integer.valueOf(startX + radius), Integer.valueOf(maxY), Integer.valueOf(startZ + radius),
      world.dimension().location().toString
    ), true)

    ctx.getSource.sendSuccess(Text.translate("bdlib.oredistribution.start2"), true)

    val oreTags = BlockTags.getAllTags.getAllTags.asScala.flatMap({
      case (tag, blocks) if tag.getPath.startsWith("ores/") => Some(tag.getPath.substring(5) -> blocks.getValues.asScala.toSet)
      case _ => None
    }).toMap

    val ores = oreTags.flatMap({ case (ore, blocks) => blocks.map(b => b -> ore) })

    val distribution = mutable.Map.empty[String, Int].withDefaultValue(0)
    val kinds = oreTags.keys.map(_ -> mutable.Set.empty[Block]).toMap

    for (bp <- new BlockPos(startX - radius, minY, startZ - radius) to new BlockPos(startX + radius, maxY, startZ + radius)) {
      val block = world.getBlockState(bp).getBlock
      for (ore <- ores.get(block)) {
        distribution(ore) += 1
        kinds(ore).add(block)
      }
    }

    val total = distribution.values.sum

    val warnings = kinds
      .map({ case (ore, blocks) => ore -> blocks.groupBy(_.getRegistryName.getNamespace) })
      .filter(_._2.size > 1)

    if (toFile) {
      // === OUTPUT TO FILE ===
      val dumpFile = FMLPaths.GAMEDIR.get().resolve("ore_distribution.txt").toFile
      Using(new BufferedWriter(new FileWriter(dumpFile))) { dumpWriter =>
        dumpWriter.write("Ore distribution report - (%d,%d,%d) to (%d,%d,%d) dim %s".format(
          startX - radius, minY, startZ - radius, startX + radius, maxY, startZ + radius, world.dimension().location().toString
        ))
        dumpWriter.newLine()
        dumpWriter.newLine()
        if (warnings.nonEmpty) {
          for ((id, types) <- warnings.toList.sortBy(_._1)) {
            dumpWriter.write("Warning: %s has multiple variants generated:".format(id))
            dumpWriter.newLine()
            for ((mod, blocks) <- types) {
              dumpWriter.write(" - %s (%s)".format(mod, blocks.map(_.getRegistryName.getPath).mkString(", ")))
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
        ctx.getSource.sendSuccess(Text.translate("bdlib.oredistribution.saved", dumpFile.getCanonicalPath), true)
      } recover {
        case e: Throwable =>
          ctx.getSource.sendFailure(Text.translate("bdlib.oredistribution.error", e.toString))
          BdLib.logErrorException("Failed to save ore distribution dump", e)
      }
    } else {
      // === OUTPUT TO CHAT ===
      for ((id, types) <- warnings.toList.sortBy(_._1)) {
        ctx.getSource.sendSuccess(Text.translate("bdlib.oredistribution.warn",
          Text.string(" !").withStyle(ChatFormatting.RED),
          Text.string(id).withStyle(ChatFormatting.RED),
        ), true)
        for ((mod, blocks) <- types)
          ctx.getSource.sendSuccess(
            Text.string(" - ").withStyle(ChatFormatting.RED)
              .append(Text.string(mod).withStyle(ChatFormatting.YELLOW))
              .append(
                Text.string(" (%s)".format(blocks.map(_.getRegistryName.getPath).mkString(", ")))
                  .withStyle(ChatFormatting.GRAY)
              ), true)
      }
      for ((id, num) <- distribution.filter(_._2 > 0).toList.sortBy(-_._2)) {
        ctx.getSource.sendSuccess(Text.translate("bdlib.oredistribution.entry",
          " *",
          Text.string(id).withStyle(ChatFormatting.YELLOW),
          DecFormat.round(num),
          DecFormat.short(100F * num / total)
        ), true)
      }
    }
    Command.SINGLE_SUCCESS
  }
}
