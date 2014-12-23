/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.recipes

import java.io.Reader

import net.minecraftforge.oredict.OreDictionary
import org.apache.commons.lang3.StringEscapeUtils

import scala.util.parsing.combinator._

class RecipeParser extends JavaTokenParsers {
  // Allows C-style comments
  protected override val whiteSpace = """(\s|//.*|(?m)/\*(\*(?!/)|[^*])*\*/)+""".r

  // Simple integer without signs
  def int = "\\d+".r ^^ (_.toInt)

  // Possible metadata suffix (defaults to wildcard value)
  def maybeMeta = ("@" ~> int).? ^^ (_.getOrElse(OreDictionary.WILDCARD_VALUE))

  // Possible modId prefix (defaults to minecraft)
  def maybeModId = (str <~ ":").? ^^ (_.getOrElse("minecraft"))

  // Qualified class name
  def clsPath = (ident <~ ".").* ~ ident

  // Single character usable in recipes (this could be extended)
  def recipeChar = "[a-zA-Z0-9]".r ^^ (_.charAt(0))

  // Set of 3 characters for 3x3 recipes (underscore = empty)
  def recipeTriplet = repN(3, recipeChar | "_") ^^ (_.mkString(""))

  // Set of 2 characters for 2x2 recipes
  def recipeDuplet = repN(2, recipeChar | "_") ^^ (_.mkString(""))

  // Quoted string with escape characters
  def unescapeStr = stringLiteral ^^ { case x => StringEscapeUtils.unescapeJava(x.substring(1, x.length - 1)) }

  // Identifier or quoted string
  def str = ident | unescapeStr

  // === Item references ===

  def specOD = "OD" ~> ":" ~> ident ^^ StackOreDict
  def specBlock = "B" ~> ":" ~> maybeModId ~ str ~ maybeMeta ^^ { case m ~ n ~ i => StackBlock(m, n, i) }
  def specItem = "I" ~> ":" ~> maybeModId ~ str ~ maybeMeta ^^ { case m ~ n ~ i => StackItem(m, n, i) }
  def specGenericStack = "S" ~> ":" ~> str ~ ":" ~ str ^^ { case m ~ c ~ n => StackGeneric(m, n) }
  def specMacro = "$" ~> recipeChar ^^ StackMacro

  def specReflect = clsPath ~ ("[" ~> ident <~ "]") ~ maybeMeta ^^ {
    case (p ~ cl) ~ id ~ m => StackReflect((p :+ cl).mkString("."), id, m)
  }

  def specGetter = clsPath ~ ("(" ~> str <~ ")") ~ maybeMeta ^^ {
    case (p ~ cl) ~ n ~ m => StackGetter(p.mkString("."), cl, n, m)
  }

  def spec = specBlock | specItem | specOD | specGenericStack | specGetter | specReflect | specMacro

  // Spec with possible number of items
  def specWithCount = spec ~ ("*" ~> int).?

  // === Conditions ===

  def cndHaveMod = "HaveMod" ~> str ^^ CndHaveMod
  def cndHaveAPI = "HaveAPI" ~> str ^^ CndHaveAPI
  def cndHaveOD = "HaveOD" ~> str ^^ CndHaveOD

  def cndNOT = "!" ~> condition ^^ CndNOT
  def cndOR = "(" ~> condition ~ "||" ~ condition <~ ")" ^^ { case c1 ~ or ~ c2 => CndOR(c1, c2) }
  def cndAND = "(" ~> condition ~ "&&" ~ condition <~ ")" ^^ { case c1 ~ and ~ c2 => CndAND(c1, c2) }

  def condition: Parser[Condition] = cndHaveAPI | cndHaveMod | cndHaveOD | cndNOT | cndOR | cndAND

  // === Blocks ===

  def recipesSubBlock = "recipes" ~> ("{" ~> recipeStatements <~ "}") ^^ RsRecipes
  def recipesTopBlock = "recipes" ~> ("{" ~> recipeStatements <~ "}") ^^ CsRecipeBlock

  def conditionRecipes = "if" ~> condition ~ ("{" ~> recipeStatements <~ "}") ~ ("else" ~> "{" ~> recipeStatements <~ "}").? ^^ {
    case cond ~ thn ~ els => new RsConditional(cond, thn, els.getOrElse(List.empty))
  }

  def conditionConfig = "if" ~> condition ~ ("<<" ~> configStatements <~ ">>") ~ ("else" ~> "<<" ~> configStatements <~ ">>").? ^^ {
    case cond ~ thn ~ els => new CsConditionalConfig(cond, thn, els.getOrElse(List.empty))
  }

  def conditionBridge = conditionRecipes ^^ { case cond => CsRecipeBlock(List(cond)) }

  // === Statements ===

  def charSpec = recipeChar ~ "=" ~ spec ^^ {
    case ch ~ eq ~ spec => new RsCharAssign(ch, spec)
  }

  def classMacro = ("def" ~> ident <~ "=") ~ clsPath ^^ {
    case id ~ (p ~ cl) => RsClassMacro(id, (p :+ cl).mkString("."))
  }

  def recipeShaped3x3 = recipeTriplet ~ recipeTriplet ~ ("=>" ~> specWithCount) ~ recipeTriplet ^^ {
    case r1 ~ r2 ~ (res ~ n) ~ r3 => RsRecipeShaped(Seq(r1, r2, r3), res, n.getOrElse(1))
  }

  def recipeShaped2x2 = recipeDuplet ~ recipeDuplet ~ ("=>" ~> specWithCount) ^^ {
    case r1 ~ r2 ~ (res ~ n) => RsRecipeShaped(Seq(r1, r2), res, n.getOrElse(1))
  }

  def recipeShaped9 = recipeTriplet ~ recipeTriplet ~ recipeTriplet ~ ("=>" ~> specWithCount) ^^ {
    case r1 ~ r2 ~ r3 ~ (res ~ n) => RsRecipeShaped(Seq(r1, r2, r3), res, n.getOrElse(1))
  }

  def recipeShapeless = "shapeless" ~> ":" ~> recipeChar.+ ~ ("=>" ~> specWithCount) ^^ {
    case r ~ (res ~ n) => RsRecipeShapeless(r.mkString(""), res, n.getOrElse(1))
  }

  def recipeSmelting = "smelt" ~> ":" ~> spec ~ ("=>" ~> specWithCount) ~ ("+" ~> decimalNumber <~ "xp").? ^^ {
    case in ~ (out ~ n) ~ xp => RsRecipeSmelting(in, out, n.getOrElse(1), xp.getOrElse("0").toFloat)
  }

  def regOreDict = "regOreDict" ~> ":" ~> spec ~ "@WILDCARD".? ~ ("=>" ~> str) ^^ {
    case spec ~ wildcard ~ id => RsRegOredict(id, spec, wildcard.isDefined)
  }

  def clearRecipes = "clearRecipes" ~> ":" ~> spec ^^ { case sp => CsClearRecipes(sp) }

  def recipeStatement: Parser[RecipeStatement] = (
    charSpec
      | classMacro
      | recipesSubBlock
      | conditionRecipes
      | recipesSubBlock
      | recipeShaped3x3
      | recipeShaped2x2
      | recipeShaped9
      | recipeShapeless
      | recipeSmelting
      | regOreDict
    )

  def configStatement: Parser[ConfigStatement] = (
    recipesTopBlock
      | clearRecipes
      | conditionBridge
      | conditionConfig
    )

  def configStatements = configStatement.*
  def recipeStatements = recipeStatement.*

  def doParse(r: Reader): List[ConfigStatement] = {
    parseAll(configStatements, r) match {
      case Success(res, next) => return res
      case NoSuccess(msg, next) => sys.error("Config parsing failed at %s: %s".format(next.pos, msg))
    }
  }
}
