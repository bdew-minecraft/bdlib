package net.bdew.lib.rich

import net.bdew.lib.nbt.Type
import net.minecraft.nbt.{CompoundTag, ListTag}
import net.minecraft.world.item.ItemStack

import scala.language.implicitConversions

class RichCompoundNBT(val tag: CompoundTag) extends AnyVal {
  def getVal[T: Type](name: String): Option[T] = {
    val vtype = Type[T]
    Option(tag.get(name)) flatMap (v => vtype.toVal(v))
  }

  def getVal[T: Type](name: String, default: => T): T =
    getVal(name).getOrElse(default)

  def setVal[T: Type](name: String, value: T): Unit = {
    tag.put(name, Type[T].toNBT(value))
  }

  def getListVals[T: Type](name: String): Iterable[T] = {
    val vtype = Type[T]
    val list = tag.getList(name, vtype.id)
    for (i <- 0 until list.size(); element <- vtype.toVal(list.get(i))) yield element
  }

  def setListVals[T: Type](name: String, v: Iterable[T]): Unit = {
    val list = new ListTag()
    for (x <- v) list.add(Type[T].toNBT(x))
    tag.put(name, list)
  }

  def toItemStack: Some[ItemStack] = Some(ItemStack.of(tag))
}