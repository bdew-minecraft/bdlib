package net.bdew.lib.keepdata

import net.bdew.lib.nbt.NBT
import net.bdew.lib.tile.TileExtended
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT

/**
 * Mixin for tile entities to keep their data when broken
 * Block class has to extend BlockKeepData
 */
trait TileKeepData extends TileExtended {
  /**
   * Override to modify data that will be stored in an item
   *
   * @param t Automatically serialized data
   * @return Modified serialized data
   */
  def afterTileBreakSave(t: CompoundNBT) = t

  /**
   * Override to modify data that will be load from an item
   * After the tile entity is fully constructed and added to world
   *
   * @param t Automatically serialized data
   * @return Modified serialized data
   */
  def beforeTileBreakLoad(t: CompoundNBT) = t

  final def saveToItem(is: ItemStack): Unit = {
    val tag = is.getOrCreateTag()
    tag.put("data", afterTileBreakSave(NBT.from(persistSave.trigger)))
  }

  final def loadFromItem(is: ItemStack): Unit = {
    if (is.hasTag && is.getTag.contains("data")) {
      persistLoad.trigger(beforeTileBreakLoad(is.getTag.getCompound("data")))
    }
  }
}
