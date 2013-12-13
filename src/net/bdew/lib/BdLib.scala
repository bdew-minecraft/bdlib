package net.bdew.lib

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.event.FMLPreInitializationEvent

@Mod(modid = "bdlib", name="BD lib", version = "BDLIB_VER", modLanguage =  "scala")
object BdLib {
  def preInit(ev: FMLPreInitializationEvent) {
    ev.getModLog.info("bdlib BDLIB_VER loaded")
  }
}
