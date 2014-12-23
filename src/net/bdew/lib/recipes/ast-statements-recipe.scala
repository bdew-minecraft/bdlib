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
 * Base class for recipe statements
 * Actual processing is in [[net.bdew.lib.recipes.RecipeLoader.processRecipeStatements]]
 *
 * Naming convention: All subclasses are named RsFoo
 */
abstract class RecipeStatement

/**
 * Character assignment
 * Syntax: {char} = {ref}
 * Parser: [[net.bdew.lib.recipes.RecipeParser.charSpec]]
 * @param char Character to assign
 * @param spec ItemStack Reference
 */
case class RsCharAssign(char: Char, spec: StackRef) extends RecipeStatement

/**
 * Define class macro
 * Syntax: def {id} = {name}
 * Parser: [[net.bdew.lib.recipes.RecipeParser.classMacro]]
 * @param id Macro identifier
 * @param cls Class name
 */
case class RsClassMacro(id: String, cls: String) extends RecipeStatement

/**
 * Unconditional block - allows grouping and isolation of statements
 * Syntax: recipes { {statements} }
 * Parser: [[net.bdew.lib.recipes.RecipeParser.recipesSubBlock]]
 * @param list Statements to run
 */
case class RsRecipes(list: List[RecipeStatement]) extends RecipeStatement

/**
 * Conditional recipes block
 * Syntax: if {condition} recipes { {statements} } [else { {statements} }]
 * Parser: [[net.bdew.lib.recipes.RecipeParser.conditionRecipes]]
 * @param cond Condition
 * @param thn Statements to run if true
 * @param els Statements to run otherwise
 */
case class RsConditional(cond: Condition, thn: List[RecipeStatement], els: List[RecipeStatement]) extends RecipeStatement

/**
 * Base class for statements that represent a crafting recipe, that has a result
 */
abstract class CraftingStatement extends RecipeStatement {
  def result: StackRef
}

/**
 * Shaped recipe definition
 *
 * Syntax:
 * XXX
 * XXX => {result} [* {count}]
 * XXX
 *
 * Parser: [[net.bdew.lib.recipes.RecipeParser.recipeShaped3x3]]
 *
 * Syntax:
 * XX
 * XX => {result} [* {count}]
 *
 * Parser: [[net.bdew.lib.recipes.RecipeParser.recipeShaped2x2]]
 *
 * Syntax: XXX XXX XXX => {result} [* {count}]
 * Parser: [[net.bdew.lib.recipes.RecipeParser.recipeShaped9]]
 *
 * @param rec Recipe pattern
 * @param result Result reference
 * @param cnt Number of output items
 */
case class RsRecipeShaped(rec: Seq[String], result: StackRef, cnt: Int) extends CraftingStatement

/**
 * Shapeless recipe definition
 * Syntax: shapeless: {characters} => {result} [* {count}]
 * Parser: [[net.bdew.lib.recipes.RecipeParser.recipeShapeless]]
 *
 * @param rec Recipe pattern
 * @param result Result reference
 * @param cnt Number of output items
 */
case class RsRecipeShapeless(rec: String, result: StackRef, cnt: Int) extends CraftingStatement

/**
 * Smelting recipe definition
 * Syntax: smelt: {input} => {output}, {XP}
 * Parser [[net.bdew.lib.recipes.RecipeParser.recipeSmelting]]
 *
 * @param in Input Stack
 * @param result Output Stack
 * @param cnt Output Count
 * @param xp XP for smelting
 */
case class RsRecipeSmelting(in: StackRef, result: StackRef, cnt: Int, xp: Float) extends CraftingStatement

/**
 * Smelting recipe definition
 * Syntax: regOreDict: {reference} -> {name}
 * Parser [[net.bdew.lib.recipes.RecipeParser.regOreDict]]
 */
case class RsRegOredict(id: String, spec: StackRef, wildcard: Boolean) extends RecipeStatement

