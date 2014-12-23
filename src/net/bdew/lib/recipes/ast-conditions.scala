/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.recipes

/**
 * Represents a condition, something that can be resolved to a boolean
 * Actual processing is in [[RecipeLoader.resolveCondition]]
 *
 * Naming convention: All subclasses are named CndFoo
 */
abstract class Condition

/**
 * True if mod is present
 * Syntax: HaveMod {string}
 * Parser: [[RecipeParser.cndHaveMod]]
 * @param mod mod id
 */
case class CndHaveMod(mod: String) extends Condition

/**
 * True if API is present
 * Syntax: HaveAPI {string}
 * Parser: [[RecipeParser.cndHaveAPI]]
 */
case class CndHaveAPI(api: String) extends Condition

/**
 * True if oredict entry is present
 * Syntax: HaveOD {string}
 * Parser: [[RecipeParser.cndHaveOD]]
 */
case class CndHaveOD(od: String) extends Condition

/**
 * Boolean NOT
 * Syntax: ! {condition}
 * Parser: [[RecipeParser.cndNOT]]
 */
case class CndNOT(cnd: Condition) extends Condition

/**
 * Boolean AND
 * Syntax: ( {condition} && {condition} )
 * Parser: [[RecipeParser.cndAND]]
 */
case class CndAND(c1: Condition, c2: Condition) extends Condition

/**
 * Boolean OR
 * Syntax: ( {condition} || {condition} )
 * Parser: [[RecipeParser.cndOR]]
 */
case class CndOR(c1: Condition, c2: Condition) extends Condition

