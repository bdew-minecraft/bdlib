package net.bdew.lib.multiblock.data

import net.bdew.lib.gui.Texture
import net.minecraftforge.api.distmarker.{Dist, OnlyIn}

abstract class SlotSet(val outputConfigId: String) {
  // outputConfigId should be registered in OutputConfigManager

  var slotMap = Map.empty[String, Slot]

  case class Slot(id: String, name: String) {
    slotMap += id -> this
    def next: Slot = order(this)

    @OnlyIn(Dist.CLIENT)
    def texture: Texture = textures(this)
  }

  def default: Slot
  val order: Map[Slot, Slot]

  @OnlyIn(Dist.CLIENT)
  def textures: Map[Slot, Texture]

  def get(id: String): Slot = slotMap(id)
}
