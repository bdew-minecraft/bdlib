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
 * Represents something that can be resolved to ItemStack or recipe component
 * Actual processing is in:
 * * [[net.bdew.lib.recipes.RecipeLoader.getRecipeComponent]]
 * * [[net.bdew.lib.recipes.RecipeLoader.getConcreteStack]]
 * * [[net.bdew.lib.recipes.RecipeLoader.getAllConcreteStacks]]
 *
 * Naming convention: All subclasses are named StackFoo
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
 * Generic Reference
 * Syntax: S:{modId}:{name}
 * Parser: [[net.bdew.lib.recipes.RecipeParser.specGenericStack]]
 * @param mod modId of owner mod
 * @param name name of the stack
 */
case class StackGeneric(mod: String, name: String) extends StackRef

/**
 * Block reference
 * Syntax: B:[{modId}:]{name} [@ {meta}]
 * Parser: [[net.bdew.lib.recipes.RecipeParser.specBlock]]
 * @param mod modId of owner mod, default is "minecraft"
 * @param name Registered name
 * @param meta Metadata / Damage
 */
case class StackBlock(mod: String, name: String, meta: Int) extends StackRef

/**
 * Item reference
 * Syntax: I:[{modId}:]{name} [@ {meta}]
 * Parser: [[net.bdew.lib.recipes.RecipeParser.specItem]]
 * @param mod modId of owner mod, default is "minecraft"
 * @param name Registered name
 * @param meta Metadata / Damage
 */
case class StackItem(mod: String, name: String, meta: Int) extends StackRef

/**
 * Reference to an Item/Block/ItemStack public static field
 * Syntax: {path}.{to}.{class}[{field}] [@ {meta}]
 * Parser: [[net.bdew.lib.recipes.RecipeParser.specReflect]]
 * @param cls Class name
 * @param field Field name
 * @param meta Metadata / Damage (Ignored for ItemStacks)
 */
case class StackReflect(cls: String, field: String, meta: Int) extends StackRef

/**
 * Reference to an Item/Block/ItemStack public static getter
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
