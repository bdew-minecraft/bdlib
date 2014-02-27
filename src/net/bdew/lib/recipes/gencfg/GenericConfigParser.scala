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
  def negativeNumber = "-" ~> decimalNumber ^^ { case x => "-" + x }
  def signedNumber = decimalNumber | negativeNumber

  def cfgStatementNum = ident ~ "=" ~ signedNumber ^^ { case id ~ eq ~ n => CfgVal(id, EntryDouble(n.toDouble)) }
  def cfgStatementStr = ident ~ "=" ~ str ^^ { case id ~ eq ~ s => CfgVal(id, EntryStr(s)) }
  def cfgStatementNumList = ident ~ ("=" ~> "{" ~> signedNumber.* <~ "}") ^^ { case id ~ l => CfgVal(id, EntryNumList(l.map(_.toDouble))) }
  def cfgStatementSub = cfgBlock ^^ { case (id, st) => CfgSub(id, st) }

  def cfgStatement = cfgStatementNum | cfgStatementStr | cfgStatementSub | cfgStatementNumList

  // A bit of type acrobatics because this is recursive (via cfgStatementSub)
  // the tuple is translated into the proper class in either cfgStatementSub or cfg
  def cfgBlock: Parser[(String, List[CfgStatement])] =
    "cfg" ~> ident ~ ("{" ~> cfgStatement.* <~ "}") ^^ { case id ~ st => (id, st) }

  def statementCfg = cfgBlock ^^ { case (id, st) => StCfg(id, st) }
}
