package net.bdew.lib.items

import net.bdew.lib.nbt.Type
import net.minecraft.item.ItemStack

class StackProperty[T: Type](name: String) {
  val nbtType: Type[T] = Type[T]

  def exists(stack: ItemStack): Boolean = stack.hasTag && stack.getTag.contains(name, nbtType.id)

  def get(stack: ItemStack): Option[T] =
    if (exists(stack))
      nbtType.toVal(stack.getTag.get(name))
    else
      None

  def get(stack: ItemStack, default: T): T =
    get(stack).getOrElse(default)

  def set(stack: ItemStack, v: T): Unit = {
    stack.getOrCreateTag().put(name, nbtType.toNBT(v))
  }

  def delete(stack: ItemStack): Unit = {
    if (exists(stack))
      stack.getTag.remove(name)
  }
}