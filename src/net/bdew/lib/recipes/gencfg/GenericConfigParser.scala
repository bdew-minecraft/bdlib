/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.recipes.gencfg

import net.bdew.lib.recipes.RecipeParser

trait GenericConfigParser extends RecipeParser {
  override def statement = statementCfg | super.statement

  // Hex variant must come before decimalNumber, otherwise the parser breaks
  def signedNumber = (
    ("0x" ~> "[0-9A-Fa-f]+".r) ^^ { case x => Integer.parseInt(x, 16).toDouble }
      | decimalNumber ^^ { case x => x.toDouble }
      | ("-" ~> decimalNumber) ^^ { case x => -x.toDouble }
    )

  def cfgStatementNum = str ~ "=" ~ signedNumber ^^ { case id ~ eq ~ n => CfgVal(id, EntryDouble(n.toDouble)) }
  def cfgStatementStr = str ~ "=" ~ str ^^ { case id ~ eq ~ s => CfgVal(id, EntryStr(s)) }
  def cfgStatementNumList = str ~ ("=" ~> "{" ~> signedNumber.* <~ "}") ^^ { case id ~ l => CfgVal(id, EntryNumList(l)) }
  def cfgStatementSub = cfgBlock ^^ { case (id, st) => CfgSub(id, st) }

  def cfgStatement = cfgStatementNum | cfgStatementStr | cfgStatementSub | cfgStatementNumList

  // A bit of type acrobatics because this is recursive (via cfgStatementSub)
  // the tuple is translated into the proper class in either cfgStatementSub or cfg
  def cfgBlock: Parser[(String, List[CfgStatement])] =
    "cfg" ~> str ~ ("{" ~> cfgStatement.* <~ "}") ^^ { case id ~ st => (id, st) }

  def statementCfg = cfgBlock ^^ { case (id, st) => StCfg(id, st) }
}
