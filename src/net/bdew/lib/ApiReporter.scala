/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib

import java.util
import java.util.Locale

import cpw.mods.fml.common.{ICrashCallable, ModAPIManager, ModContainer}

import scala.collection.JavaConversions._

object ApiReporter extends ICrashCallable {
  lazy val APIs = {
    val mods = new util.ArrayList[ModContainer]
    val nameLookup = new util.HashMap[String, ModContainer]
    ModAPIManager.INSTANCE.injectAPIModContainers(mods, nameLookup)
    (for (mod <- mods.sortBy(_.getModId.toLowerCase(Locale.US))) yield
      "\t\t* %s (%s) from %s".format(mod.getModId, mod.getVersion,
        if (mod.getSource.isDirectory)
          mod.getSource
        else
          mod.getSource.getName
      )).mkString(Misc.lineSeparator)
  }
  override def getLabel = "List of loaded APIs"
  override def call() = Misc.lineSeparator + APIs
}
