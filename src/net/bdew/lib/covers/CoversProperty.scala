package net.bdew.lib.covers

import net.bdew.lib.property.SimpleUnlistedProperty
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing

object CoversProperty extends SimpleUnlistedProperty("COVERS", classOf[Map[EnumFacing, ItemStack]])