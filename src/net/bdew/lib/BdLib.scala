package net.bdew.lib

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.event.FMLPreInitializationEvent

@Mod(modid = "bdlib", version = "@BdLibVer@", modLanguage =  "scala")
object BdLib {
  def preInit(ev: FMLPreInitializationEvent) {
    ev.getModLog.info("bdlib @BdLibVer@ loaded")
  }
}
