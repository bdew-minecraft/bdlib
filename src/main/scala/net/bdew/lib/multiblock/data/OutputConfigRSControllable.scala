package net.bdew.lib.multiblock.data

import net.bdew.lib.misc.RSMode

trait OutputConfigRSControllable extends OutputConfig {
  var rsMode: RSMode.Value
}