package net.bdew.lib.managers

import net.minecraft.block.Block
import net.minecraft.tileentity.{TileEntity, TileEntityType}
import net.minecraftforge.fml.RegistryObject
import net.minecraftforge.registries.ForgeRegistries

class TEManager extends RegistryManager(ForgeRegistries.TILE_ENTITIES) {
  def registerWithBlock[E <: TileEntity](name: String, teFactory: TileEntityType[E] => E, blockReg: RegistryObject[_ <: Block]): RegistryObject[TileEntityType[E]] = {
    var teType: RegistryObject[TileEntityType[E]] = null // for foward reference to pass to factory
    teType =
      register(name, () => {
        val block = blockReg.get()
        val builder = TileEntityType.Builder.of[E](() => teFactory(teType.get()), block)
        val tmp = builder.build(null)
        TEManager.synchronized {
          // this will be called in parallel from different mods so needs to synchronize access
          TEManager.blockMap += (block -> tmp)
        }
        tmp
      })
    teType
  }
}

object TEManager {
  var blockMap = Map.empty[Block, TileEntityType[_]]
}