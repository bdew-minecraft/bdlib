package net.bdew.lib.commands

import com.mojang.brigadier.arguments._
import com.mojang.brigadier.builder.{LiteralArgumentBuilder, RequiredArgumentBuilder}
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.{CommandSourceStack, Commands}
import net.minecraft.server.level.ServerPlayer

class CommandArg[R](name: String, argType: ArgumentType[_], getter: (CommandContext[CommandSourceStack], String) => R) {
  def arg: RequiredArgumentBuilder[CommandSourceStack, _] = Commands.argument(name, argType)
  def get(ctx: CommandContext[CommandSourceStack]): R = getter(ctx, name)
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

  def playerArg(name: String): CommandArg[ServerPlayer] =
    new CommandArg(name, EntityArgument.player(), EntityArgument.getPlayer)

  def literal(str: String): LiteralArgumentBuilder[CommandSourceStack] = Commands.literal(str)

  def register: LiteralArgumentBuilder[CommandSourceStack]
}
