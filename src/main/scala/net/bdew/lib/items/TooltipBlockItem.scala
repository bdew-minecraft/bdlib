package net.bdew.lib.items

import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.{BlockItem, ItemStack}
import net.minecraft.util.text.ITextComponent
import net.minecraft.world.World

import java.util

trait TooltipBlockItem extends BlockItem {
  override def appendHoverText(stack: ItemStack, world: World, toolTip: util.List[ITextComponent], flags: ITooltipFlag): Unit = {
    import scala.jdk.CollectionConverters._
    toolTip.addAll(getTooltip(stack, world, flags).asJavaCollection)
    super.appendHoverText(stack, world, toolTip, flags)
  }

  def getTooltip(stack: ItemStack, world: World, flags: ITooltipFlag): List[ITextComponent] = List.empty
}
