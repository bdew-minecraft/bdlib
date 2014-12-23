/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib

import cpw.mods.fml.common.Loader
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.common.versioning.{ArtifactVersion, VersionParser}
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{IIcon, StatCollector}
import net.minecraft.world.biome.BiomeGenBase
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.{Fluid, FluidStack}
import net.minecraftforge.oredict.ShapedOreRecipe

object Misc {
  def iterNbtCompoundList(parent: NBTTagCompound, name: String): Iterable[NBTTagCompound] = {
    val list = parent.getTagList(name, 10)
    for (i <- 0 until list.tagCount()) yield list.getCompoundTagAt(i)
  }

  def iterNbtIntArray(parent: NBTTagCompound, name: String): Iterable[Array[Int]] = {
    val list = parent.getTagList(name, 11)
    for (i <- 0 until list.tagCount()) yield list.func_150306_c(i)
  }

  def getActiveModId = Loader.instance().activeModContainer().getModId

  def toLocal(s: String) = StatCollector.translateToLocal(s)
  def toLocalF(s: String, params: Any*) = StatCollector.translateToLocal(s).format(params: _*)
  def hasLocal(s: String) = StatCollector.canTranslate(s)

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

  def min[T: Ordering](values: T*) = values.min
  def max[T: Ordering](values: T*) = values.max

  def clamp[T](value: T, min: T, max: T)(implicit o: Ordering[T]): T = if (o.gt(value, max)) max else if (o.lt(value, min)) min else value

  def iterSome[T](list: Seq[T], indexes: Iterable[Int]): Iterable[T] = for (i <- indexes) yield list(i)
  def iterSomeEnum[T](list: Seq[T], indexes: Iterable[Int]): Iterable[(Int, T)] = for (i <- indexes) yield i -> list(i)

  def filterType[T](from: Iterable[_], cls: Class[T]) = from.filter(cls.isInstance).asInstanceOf[Iterable[T]]

  def getBiomeByName(name: String) = BiomeGenBase.getBiomeGenArray.find(x => x != null && x.biomeName == name).orNull

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

  // Same thing but in a way that's easier to use with map, etc.
  def asInstanceOpt[T](cls: Class[T])(v: Any) =
    if (cls.isInstance(v)) Some(v.asInstanceOf[T]) else None

  def getNeighbourTile[T](origin: TileEntity, dir: ForgeDirection, cls: Class[T]) =
    Option(origin.getWorldObj.getTileEntity(origin.xCoord + dir.offsetX,
      origin.yCoord + dir.offsetY, origin.zCoord + dir.offsetZ)) flatMap (asInstanceOpt(_, cls))

  @inline def applyMutator[T](f: (T) => Unit, init: T): T = {
    f(init)
    init
  }

  @inline def applyMutator[T](init: T)(f: (T) => Unit) = {
    f(init)
    init
  }

  def getFluidIcon(fs: FluidStack): IIcon =
    if (fs != null && fs.getFluid != null && fs.getFluid.getIcon != null)
      fs.getFluid.getIcon(fs)
    else
      Client.blockMissingIcon

  def getFluidIcon(f: Fluid): IIcon =
    if (f != null) getFluidIcon(new FluidStack(f, 1)) else Client.blockMissingIcon

  def getFluidColor(fs: FluidStack): Int =
    if (fs != null && fs.getFluid != null) fs.getFluid.getColor(fs) else 0xFFFFFF

  def getFluidColor(f: Fluid): Int =
    if (f != null) getFluidColor(new FluidStack(f, 1)) else 0xFFFFFF
}

