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
 * Base class for config statements
 * Actual processing is in [[RecipeLoader.processConfigStatement]]
 */
abstract class ConfigStatement

/**
 * Block of delayed statements, used to include recipe statements in config scope (which is always top level)
 * Syntax: recipes { {statements} }
 * Parser: [[RecipeParser.recipesTopBlock]]
 * Also used internally by the parser to bridge [[RsConditional]] to config scope in [[RecipeParser.conditionBridge]]
 * @param list Statements to run
 */
case class CsRecipeBlock(list: List[RecipeStatement]) extends ConfigStatement

/**
 * Conditional config block
 * Syntax: if {condition} << {statements} >> [else << {statements} >>]
 * Parser: [[RecipeParser.conditionConfig]]
 * @param cond Condition
 * @param thn Statements to run if true
 * @param els Statements to run otherwise
 */
case class CsConditionalConfig(cond: Condition, thn: List[ConfigStatement], els: List[ConfigStatement]) extends ConfigStatement

/**
 * Removes previously defined recipes for a specific result
 * Syntax: clearRecipes: {spec} 
 * Parser: [[RecipeParser.clearRecipes]]
 * @param res Recipes that produce this result will be cleared
 */
case class CsClearRecipes(res: StackRef) extends ConfigStatement
