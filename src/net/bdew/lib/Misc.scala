/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib

import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.StatCollector
import net.minecraft.item.ItemStack
import cpw.mods.fml.common.versioning.{ArtifactVersion, VersionParser}
import cpw.mods.fml.common.Loader
import net.minecraftforge.oredict.ShapedOreRecipe
import net.minecraft.world.biome.BiomeGenBase

object Misc {
  def iterNbtCompoundList(parent: NBTTagCompound, name: String): Iterable[NBTTagCompound] = {
    val list = parent.getTagList(name, 10)
    for (i <- 0 until list.tagCount()) yield list.getCompoundTagAt(i)
  }

  def getActiveModId = Loader.instance().activeModContainer().getModId

  def toLocal(s: String) = StatCollector.translateToLocal(s)
  def toLocalF(s: String, params: Any*) = StatCollector.translateToLocal(s).format(params: _*)

  def flattenRecipe(pattern: Seq[String], items: Map[Char, AnyRef]) =
    pattern ++ items.map {
      case (k, v) => Seq(new Character(k), v)
    }.flatten

  def wrapTag(n: String, f: (NBTTagCompound) => Any)(t: NBTTagCompound) {
    val p = t.getCompoundTag(n)
    f(p)
    t.setTag(n, p)
  }

  def addRecipe(result: ItemStack, pattern: Seq[String], items: Map[Char, AnyRef]) =
    GameRegistry.addRecipe(result, flattenRecipe(pattern, items): _*)

  def addRecipeOD(result: ItemStack, pattern: Seq[String], items: Map[Char, AnyRef]) =
    GameRegistry.addRecipe(new ShapedOreRecipe(result, flattenRecipe(pattern, items): _*))

  def min[T](vals: T*)(implicit o: Ordering[T]): T = vals.min(o)
  def max[T](vals: T*)(implicit o: Ordering[T]): T = vals.max(o)

  def clamp[T](value: T, min: T, max: T)(implicit o: Ordering[T]): T = if (o.gt(value, max)) max else if (o.lt(value, min)) min else value

  def iterSome[T](list: Seq[T], indexes: Iterable[Int]): Iterable[T] = for (i <- indexes) yield list(i)
  def iterSomeEnum[T](list: Seq[T], indexes: Iterable[Int]): Iterable[(Int, T)] = for (i <- indexes) yield i -> list(i)

  def filterType[T](from: Iterable[_], cls: Class[T]) = from.filter(cls.isInstance).asInstanceOf[Iterable[T]]

  def getBiomeByName(name: String) = BiomeGenBase.biomeList.find(_.biomeName == name).getOrElse(null)

  def haveModVersion(spec: String): Boolean = {
    val req = VersionParser.parseVersionReference(spec)
    val modid = req.getLabel

    if (!Loader.instance.getIndexedModList.containsKey(modid)) return false

    val found: ArtifactVersion = Loader.instance.getIndexedModList.get(modid).getProcessedVersion

    if (found == null || !req.containsVersion(found)) return false

    return true
  }
}
