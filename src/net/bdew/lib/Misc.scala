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
import net.minecraft.nbt.{NBTTagCompound, NBTTagList}
import net.minecraft.util.StatCollector
import net.minecraft.item.ItemStack
import cpw.mods.fml.common.versioning.{ArtifactVersion, VersionParser}
import cpw.mods.fml.common.Loader
import net.minecraftforge.oredict.ShapedOreRecipe
import net.minecraft.world.biome.BiomeGenBase
import net.minecraftforge.common.ForgeDirection
import net.minecraft.tileentity.TileEntity

object Misc {
  def iterNbtList[X](list: NBTTagList) =
    for (i <- 0 until list.tagCount()) yield list.tagAt(i).asInstanceOf[X]

  def getActiveModId = Loader.instance().activeModContainer().getModId

  def toLocal(s: String) = StatCollector.translateToLocal(s)
  def toLocalF(s: String, params: Any*) = StatCollector.translateToLocal(s).format(params: _*)
  def hasLocal(s: String) = StatCollector.func_94522_b(s)

  def flattenRecipe(pattern: Seq[String], items: Map[Char, AnyRef]) =
    pattern ++ items.map {
      case (k, v) => Seq(new Character(k), v)
    }.flatten

  def wrapTag(n: String, f: (NBTTagCompound) => Any)(t: NBTTagCompound) {
    val p = t.getCompoundTag(n)
    f(p)
    t.setCompoundTag(n, p)
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

  def getBiomeByName(name: String) = BiomeGenBase.biomeList.find(x => x != null && x.biomeName == name).getOrElse(null)

  def haveModVersion(spec: String): Boolean = {
    val req = VersionParser.parseVersionReference(spec)
    val modid = req.getLabel

    if (!Loader.instance.getIndexedModList.containsKey(modid)) return false

    val found: ArtifactVersion = Loader.instance.getIndexedModList.get(modid).getProcessedVersion

    if (found == null || !req.containsVersion(found)) return false

    return true
  }

  // Because writing this every time is awkward
  def forgeDirection(i: Int) = ForgeDirection.values()(i)

  // Easy replacement for various "if(foo.isInstanceOf[Bar]) foo.asInstanceOf[Bar]" constructs
  def asInstanceOpt[T](v: Any, cls: Class[T]) =
    if (cls.isInstance(v)) Some(v.asInstanceOf[T]) else None

  def getNeighbourTile[T](origin: TileEntity, dir: ForgeDirection, cls: Class[T]) =
    Option(origin.worldObj.getBlockTileEntity(origin.xCoord + dir.offsetX,
      origin.yCoord + dir.offsetY, origin.zCoord + dir.offsetZ)) flatMap (asInstanceOpt(_, cls))
}

