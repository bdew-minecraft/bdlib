/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.recipes.gencfg

import net.bdew.lib.recipes.RecipeParser

trait GenericConfigParser extends RecipeParser {
  override def configStatement = statementCfg | super.configStatement

  // Hex variant must come before decimalNumber, otherwise the parser breaks
  def signedNumber = (
    ("0x" ~> "[0-9A-Fa-f]+".r) ^^ { case x => Integer.parseInt(x, 16).toDouble }
      | decimalNumber ^^ { case x => x.toDouble }
      | ("-" ~> decimalNumber) ^^ { case x => -x.toDouble }
    )

  def cvNum = signedNumber ^^ EntryDouble
  def cvStr = str ^^ EntryStr
  def cvNumList = "=" ~> "{" ~> signedNumber.* <~ "}" ^^ EntryNumList

  def cfgValue = cvNum | cvStr | cvNumList

  def ceSub = cfgBlock ^^ { case (id, st) => CfgSection(id, st) }
  def ceAssign = str ~ "=" ~ cfgValue ^^ { case k ~ eq ~ v => CfgVal(k, v) }

  def cfgEntry = ceSub | ceAssign

  // A bit of type acrobatics because this is recursive (via cfgStatementSub)
  // the tuple is translated into the proper class in either cfgStatementSub or cfg
  def cfgBlock: Parser[(String, List[CfgEntry])] =
    "cfg" ~> str ~ ("{" ~> cfgEntry.* <~ "}") ^^ { case id ~ st => (id, st) }

  def statementCfg = cfgBlock ^^ { case (id, st) => CsCfgSection(id, st) }
}
