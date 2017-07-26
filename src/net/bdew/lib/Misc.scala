/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib

import java.util
import java.util.Locale

import com.google.common.base.Optional
import com.google.common.collect.ImmutableList
import net.bdew.lib.gui.Texture
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.text.translation.I18n
import net.minecraft.util.{EnumFacing, ResourceLocation}
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.fluids.{Fluid, FluidStack}
import net.minecraftforge.fml.common.versioning.VersionParser
import net.minecraftforge.fml.common.{Loader, ModAPIManager, ModContainer}

import scala.util.DynamicVariable

object Misc {
  private val forcedModId = new DynamicVariable[Option[String]](None)

  def withModId[R](s: String)(f: => R) = forcedModId.withValue(Some(s))(f)

  def getActiveModId =
    forcedModId.value.orElse(Option(Loader.instance().activeModContainer().getModId)) getOrElse "<UNKNOWN>"

  def toLocal(s: String) = I18n.translateToLocal(s)
  def toLocalF(s: String, params: Any*) = I18n.translateToLocal(s).format(params: _*)
  def hasLocal(s: String) = I18n.canTranslate(s)

  def flattenRecipe(pattern: Seq[String], items: Map[Char, AnyRef]) =
    pattern.map(_.replace('_', ' ')) ++ items.flatMap {
      case (k, v) => Seq(new Character(k), v)
    }

  def wrapTag(n: String, f: (NBTTagCompound) => Any)(t: NBTTagCompound) {
    val p = t.getCompoundTag(n)
    f(p)
    t.setTag(n, p)
  }

  def min[T: Ordering](values: T*) = values.min
  def max[T: Ordering](values: T*) = values.max

  def clamp[T](value: T, min: T, max: T)(implicit o: Ordering[T]): T = if (o.gt(value, max)) max else if (o.lt(value, min)) min else value

  def iterSome[T](list: Seq[T], indexes: Iterable[Int]): Iterable[T] = for (i <- indexes) yield list(i)
  def iterSomeEnum[T](list: Seq[T], indexes: Iterable[Int]): Iterable[(Int, T)] = for (i <- indexes) yield i -> list(i)

  def filterType[T](from: Iterable[_], cls: Class[T]) = from.filter(cls.isInstance).asInstanceOf[Iterable[T]]

  private lazy val modLookup = {
    val mods = new util.ArrayList[ModContainer]
    val nameLookup = new util.HashMap[String, ModContainer]

    nameLookup.putAll(Loader.instance().getIndexedModList)
    ModAPIManager.INSTANCE.injectAPIModContainers(mods, nameLookup)

    import scala.collection.JavaConverters._
    nameLookup.asScala
  }

  def haveModVersion(modId: String) = {
    val spec = VersionParser.parseVersionReference(modId)
    modLookup.contains(spec.getLabel) && spec.containsVersion(modLookup(spec.getLabel).getProcessedVersion)
  }

  def getModVersion(modId: String) =
    modLookup.get(modId) map (_.getVersion)

  def getModVersionString(modId: String): String = {
    val cont = modLookup.getOrElse(modId, return "NOT FOUND")
    cont.getModId + " " + cont.getVersion
  }

  // Easy replacement for various "if(foo.isInstanceOf[Bar]) foo.asInstanceOf[Bar]" constructs
  def asInstanceOpt[T](v: Any, cls: Class[T]) =
    if (cls.isInstance(v)) Some(v.asInstanceOf[T]) else None

  // Same thing but in a way that's easier to use with map, etc.
  def asInstanceOpt[T](cls: Class[T])(v: Any) =
    if (cls.isInstance(v)) Some(v.asInstanceOf[T]) else None

  def getNeighbourTile[T](origin: TileEntity, dir: EnumFacing, cls: Class[T]) =
    Option(origin.getWorld.getTileEntity(origin.getPos.offset(dir))) flatMap (asInstanceOpt(_, cls))

  def getNeighbourTileCapability[T](origin: TileEntity, dir: EnumFacing, cap: Capability[T]) =
    Option(origin.getWorld.getTileEntity(origin.getPos.offset(dir))) flatMap (tile => Option(tile.getCapability(cap, dir.getOpposite)))

  def neighbourTileHasCapability[T](origin: TileEntity, dir: EnumFacing, cap: Capability[T]) =
    Option(origin.getWorld.getTileEntity(origin.getPos.offset(dir))) exists (tile => tile.hasCapability(cap, dir.getOpposite))

  @inline def applyMutator[T](f: (T) => Unit, init: T): T = {
    f(init)
    init
  }

  @inline def applyMutator[T](init: T)(f: (T) => Unit) = {
    f(init)
    init
  }

  def nextInSeq[T](seq: Seq[T], elem: T) = {
    val pos = seq.indexOf(elem)
    if (pos < 0 || pos == seq.size - 1)
      seq.head
    else
      seq(pos + 1)
  }

  def prevInSeq[T](seq: Seq[T], elem: T) = {
    val pos = seq.indexOf(elem)
    if (pos <= 0)
      seq.last
    else
      seq(pos - 1)
  }

  def getFluidIcon(fs: FluidStack): Texture =
    if (fs != null && fs.getFluid != null && fs.getFluid.getStill(fs) != null)
      Texture(Texture.BLOCKS, Client.textureMapBlocks.getAtlasSprite(fs.getFluid.getStill(fs).toString))
    else
      Texture(Texture.BLOCKS, Client.missingIcon)

  def getFluidIcon(f: Fluid): Texture =
    if (f != null && f.getStill() != null)
      Texture(Texture.BLOCKS, Client.textureMapBlocks.getAtlasSprite(f.getStill.toString))
    else
      Texture(Texture.BLOCKS, Client.missingIcon)

  def getFluidColor(fs: FluidStack): Int =
    if (fs != null && fs.getFluid != null) fs.getFluid.getColor(fs) else 0xFFFFFF

  def getFluidColor(f: Fluid): Int =
    if (f != null) getFluidColor(new FluidStack(f, 1)) else 0xFFFFFF

  lazy val lineSeparator = System.getProperty("line.separator")

  /**
    * Provides something like java try-with-resources in scala
    */
  def withAutoClose[T <: AutoCloseable, R](o: T)(f: T => R): R = {
    val res = try {
      f(o)
    } catch {
      case t1: Throwable =>
        if (o != null) {
          try {
            o.close()
          } catch {
            case t2: Throwable =>
              t1.addSuppressed(t2)
          }
        }
        throw t1
    }
    if (o != null)
      o.close()
    res
  }

  def iconName(domain: String, name: String, path: String*) =
    new ResourceLocation(domain,
      if (path.isEmpty)
        name.toLowerCase(Locale.US)
      else
        "%s/%s".format(name, path.mkString("/")).toLowerCase(Locale.US)
    )

  def toOptional[T](o: Option[T]): Optional[T] = o match {
    case Some(v) => Optional.of(v)
    case None => Optional.absent()
  }

  def jImmutable[V](i: Iterable[V]): ImmutableList[V] = {
    import scala.collection.JavaConverters._
    ImmutableList.copyOf(i.asJava)
  }
}

