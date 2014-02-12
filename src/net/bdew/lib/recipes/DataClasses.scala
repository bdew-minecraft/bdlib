/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.recipes

/**
 * Base class for everything that can be resolved to ItemStack or recipe component
 * Actual processing is in:
 * * [[net.bdew.lib.recipes.RecipeLoader.getRecipeComponent]]
 * * [[net.bdew.lib.recipes.RecipeLoader.getConcreteStack]]
 * * [[net.bdew.lib.recipes.RecipeLoader.getAllConcreteStacks]]
 *
 */
abstract class StackRef

/**
 * Ore Dictionary reference
 * Syntax: OD:{identifier}
 * Parser: [[net.bdew.lib.recipes.RecipeParser.specOD]]
 * @param id OD entry name
 */
case class StackOreDict(id: String) extends StackRef

/**
 * Generic Referende
 * Syntax: S:{modid}:{name}
 * Parser: [[net.bdew.lib.recipes.RecipeParser.specGenericStack]]
 * @param mod modId of owner mod
 * @param name name of the stack
 */
case class StackGeneric(mod: String, name: String) extends StackRef

/**
 * Block reference
 * Syntax: B:[{modid}:]{name} [@ {meta}]
 * Parser: [[net.bdew.lib.recipes.RecipeParser.specBlock]]
 * @param mod modId of owner mod, default is "minecraft"
 * @param name Registered name
 * @param meta Metadata / Damage
 */
case class StackBlock(mod: String, name: String, meta: Int) extends StackRef

/**
 * Item reference
 * Syntax: I:[{modid}:]{name} [@ {meta}]
 * Parser: [[net.bdew.lib.recipes.RecipeParser.specItem]]
 * @param mod modId of owner mod, default is "minecraft"
 * @param name Registered name
 * @param meta Metadata / Damage
 */
case class StackItem(mod: String, name: String, meta: Int) extends StackRef

/**
 * Reference to an Item/Block/ItemStack pulic static field
 * Syntax: {path}.{to}.{class}[{field}] [@ {meta}]
 * Parser: [[net.bdew.lib.recipes.RecipeParser.specReflect]]
 * @param cls Class name
 * @param field Field name
 * @param meta Metadata / Damage (Ignored for ItemStacks)
 */
case class StackReflect(cls: String, field: String, meta: Int) extends StackRef

/**
 * Reference to an Item/Block/ItemStack pulic static getter
 * Syntax: {path}.{to}.{class}.{method}({string}) [@ {meta}]
 * Parser: [[net.bdew.lib.recipes.RecipeParser.specGetter]]
 * @param cls Class name
 * @param method Method name
 * @param name Parameter to getter
 * @param meta Metadata / Damage (Ignored for ItemStacks)
 */
case class StackGetter(cls: String, method: String, name: String, meta: Int) extends StackRef

/**
 * Macro reference to a previous definition
 * Syntax: ${char}
 * Parser: [[net.bdew.lib.recipes.RecipeParser.specMacro]]
 * @param id The underlying character
 */
case class StackMacro(id: Char) extends StackRef

/**
 * Base class for statements
 * Actual processing is in [[net.bdew.lib.recipes.RecipeLoader.processStatement]]
 */
abstract class Statement

/**
 * Base class for delayed statements (executed after everything is parsed)
 * Actual processing is in [[net.bdew.lib.recipes.RecipeLoader.processDelayedStatements]]
 */
abstract class DelayedStatement extends Statement

/**
 * Character assignment
 * Syntax: {char} = {ref}
 * Parser: [[net.bdew.lib.recipes.RecipeParser.charSpec]]
 * @param char Character to assign
 * @param spec ItemStack Reference
 */
case class StCharAssign(char: Char, spec: StackRef) extends DelayedStatement

/**
 * Define class macro
 * Syntax: def {id} = {name}
 * Parser: [[net.bdew.lib.recipes.RecipeParser.classMacro]]
 * @param id Macro identifier
 * @param cls Class name
 */
case class StClassMacro(id: String, cls: String) extends DelayedStatement

/**
 * Conditional block - checks if mod is loaded
 * Syntax: ifMod {string} { {statements} } [else { {statements} }]
 * Parser: [[net.bdew.lib.recipes.RecipeParser.ifMod]]
 * @param mod mod and version to check [[cpw.mods.fml.common.versioning.VersionParser]]
 * @param thn Statements to run if present
 * @param els Statements to run otherwise
 */
case class StIfHaveMod(mod: String, thn: List[Statement], els: List[Statement]) extends Statement

/**
 * Conditional block - checks if mod is loaded
 * Syntax: ifOreDict {string} { {statements} } [else { {statements} }]
 * Parser: [[net.bdew.lib.recipes.RecipeParser.ifOreDict]]
 * @param id Ore Dictionary Entry
 * @param thn Statements to run if present
 * @param els Statements to run otherwise
 */
case class StIfHaveOD(id: String, thn: List[Statement], els: List[Statement]) extends Statement

/**
 * Removes previously defined recieps for a specific result
 * @param res Recipes that produce this result will be cleared
 */
case class StClearRecipes(res: StackRef) extends Statement

/**
 * Base class for staments that represent a crafting recipe
 */
abstract class CraftingStatement extends DelayedStatement {
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
case class StRecipeShaped(rec: Seq[String], result: StackRef, cnt: Int) extends CraftingStatement

/**
 * Shapeless recipe definition
 * Syntax: shapeless: {characters} => {result} [* {count}]
 * Parser: [[net.bdew.lib.recipes.RecipeParser.recipeShapeless]]
 *
 * @param rec Recipe pattehrn
 * @param result Result reference
 * @param cnt Number of output items
 */
case class StRecipeShapeless(rec: String, result: StackRef, cnt: Int) extends CraftingStatement

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
case class StSmeltRecipe(in: StackRef, result: StackRef, cnt: Int, xp: Float) extends CraftingStatement

/**
 * Used in statement processing to abort the current statement and show an error without a stack trace
 */
class StatementError(msg: String) extends Exception(msg)
