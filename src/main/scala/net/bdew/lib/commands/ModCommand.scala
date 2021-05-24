package net.bdew.lib.commands

import com.mojang.brigadier.arguments._
import com.mojang.brigadier.builder.{LiteralArgumentBuilder, RequiredArgumentBuilder}
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.arguments.EntityArgument
import net.minecraft.command.{CommandSource, Commands}
import net.minecraft.entity.player.ServerPlayerEntity

class CommandArg[R](name: String, argType: ArgumentType[_], getter: (CommandContext[CommandSource], String) => R) {
  def arg: RequiredArgumentBuilder[CommandSource, _] = Commands.argument(name, argType)
  def get(ctx: CommandContext[CommandSource]): R = getter(ctx, name)
}

trait ModCommand {
  def intArg(name: String, min: Int = Int.MinValue, max: Int = Int.MaxValue): CommandArg[Int] =
    new CommandArg(name, IntegerArgumentType.integer(min, max), IntegerArgumentType.getInteger)

  def longArg(name: String, min: Long = Long.MinValue, max: Long = Long.MaxValue): CommandArg[Long] =
    new CommandArg(name, LongArgumentType.longArg(min, max), LongArgumentType.getLong)

  def floatArg(name: String, min: Float = Float.MinValue, max: Float = Float.MaxValue): CommandArg[Float] =
    new CommandArg(name, FloatArgumentType.floatArg(min, max), FloatArgumentType.getFloat)

  def doubleArg(name: String, min: Double = Double.MinValue, max: Double = Double.MaxValue): CommandArg[Double] =
    new CommandArg(name, DoubleArgumentType.doubleArg(min, max), DoubleArgumentType.getDouble)

  def stringArg(name: String): CommandArg[String] =
    new CommandArg(name, StringArgumentType.string(), StringArgumentType.getString)

  def wordArg(name: String): CommandArg[String] =
    new CommandArg(name, StringArgumentType.word(), StringArgumentType.getString)

  def greedyStringArg(name: String): CommandArg[String] =
    new CommandArg(name, StringArgumentType.greedyString(), StringArgumentType.getString)

  def boolArg(name: String): CommandArg[Boolean] =
    new CommandArg(name, BoolArgumentType.bool(), BoolArgumentType.getBool)

  def playerArg(name: String): CommandArg[ServerPlayerEntity] =
    new CommandArg(name, EntityArgument.player(), EntityArgument.getPlayer)

  def literal(str: String): LiteralArgumentBuilder[CommandSource] = Commands.literal(str)

  def register: LiteralArgumentBuilder[CommandSource]
}
