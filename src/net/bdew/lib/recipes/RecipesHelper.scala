/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.recipes

import java.io._

import cpw.mods.fml.common.FMLCommonHandler
import net.bdew.lib.{BdLib, Misc}

object RecipesHelper {
  /**
   * Perform full loading of a set of config
   * @param modName Name of the mod, used in logging and errors
   * @param listResource Resource Name of the internal config list file
   * @param configDir Base path to user config giles
   * @param resBaseName Base resource name for internal config files
   * @param loader Loader instance
   */
  def loadConfigs(modName: String, listResource: String, configDir: File, resBaseName: String, loader: RecipeLoader) {
    BdLib.logInfo("Loading internal config files for mod %s", modName)

    val internals = Misc.withAutoClose(new BufferedReader(new InputStreamReader(getClass.getResourceAsStream(listResource)))) { listReader =>
      Iterator.continually(listReader.readLine)
        .takeWhile(_ != null)
        .map(_.trim)
        .filterNot(_.startsWith("#"))
        .filterNot(_.isEmpty)
        .toList
    }

    val overrideDir = new File(configDir, "overrides")
    if (!overrideDir.exists()) overrideDir.mkdir()

    for (fileName <- internals) {
      val overrideFile = new File(overrideDir, fileName)
      if (overrideFile.exists()) {
        tryLoadConfig(new FileReader(overrideFile), overrideFile.getCanonicalPath, modName, loader)
      } else {
        val url = getClass.getResource(resBaseName + fileName)
        Misc.withAutoClose(url.openStream()) { stream =>
          tryLoadConfig(new InputStreamReader(stream), url.toString, modName, loader)
        }
      }
    }

    BdLib.logInfo("Loading user config files for mod %s", modName)

    for (fileName <- configDir.list().sorted if fileName.endsWith(".cfg")) {
      val file = new File(configDir, fileName)
      if (file.canRead)
        tryLoadConfig(new FileReader(file), file.getCanonicalPath, modName, loader)
    }

    BdLib.logInfo("Config loading for mod %s finished", modName)
  }

  def tryLoadConfig(reader: Reader, path: String, modName: String, loader: RecipeLoader) {
    BdLib.logInfo("Loading config: %s", path)
    try {
      loader.load(reader)
    } catch {
      case e: Throwable =>
        FMLCommonHandler.instance().raiseException(e, "%s config loading failed in file %s: %s".format(modName, path, e.getMessage), true)
    } finally {
      reader.close()
    }
  }
}
