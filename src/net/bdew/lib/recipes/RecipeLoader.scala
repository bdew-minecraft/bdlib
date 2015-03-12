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

import cpw.mods.fml.common.ModAPIManager
import cpw.mods.fml.common.registry.GameRegistry
import net.bdew.lib.{BdLib, Misc}
import net.minecraft.block.Block
import net.minecraft.item.{Item, ItemStack}
import net.minecraftforge.oredict.{OreDictionary, ShapelessOreRecipe}

/**
 * Main recipe loader class
 * The input file is parsed by [[net.bdew.lib.recipes.RecipeParser]]
 * Into a list of [[net.bdew.lib.recipes.ConfigStatement]] subclasses
 * That are then executed here
 */

class RecipeLoader {

  /**
   * Create a new parser, override in subclasses to use an extended parser
   * @return New ConfigParser (or subclass) instance
   */
  def newParser() = new RecipeParser()

  /**
   * Current map of recipe characters to parser item references
   */
  var currCharMap = Map.empty[Char, StackRef].withDefault(x => error("Undefined recipe character '%s'", x))

  /**
   * Current map of class macros
   */
  var currClassMacros = Map.empty[String, String].withDefault(x => error("Undefined class macro '%s'", x))

  /**
   * Triggers an error with formatted string
   */
  def error(msg: String, params: Any*) = throw new StatementError(msg.format(params: _*))

  /**
   * List of unprocessed recipe statements
   */
  var recipeStatements = List.empty[RecipeStatement]

  /**
   * Looks up a recipe component
   * @param s Parser ItemStack reference
   * @return ItemStack or String that can be used as recipe components in MC functions
   */
  def getRecipeComponent(s: StackRef): AnyRef = s match {
    case StackOreDict(id) => id
    case StackMacro(ch) => getRecipeComponent(currCharMap(ch))
    case _ => getConcreteStack(s)
  }

  /**
   * Resolve class name if it's a macro
   */
  def getRealClassName(s: String) = {
    if (s.startsWith("$"))
      currClassMacros(s.stripPrefix("$"))
    else s
  }
  /**
   * Sanitize items from reflection
   * @param x The item
   * @param source Human readable source (used in errors and warnings)
   * @param meta Metadata or damage
   * @param cnt Stack size
   * @return
   */
  def sanitizeReflectedItem(x: AnyRef, source: String, meta: Int, cnt: Int): ItemStack = x match {
    case x: ItemStack =>
      val l = x.copy()
      l.stackSize = cnt
      if (meta != OreDictionary.WILDCARD_VALUE && meta != l.getItemDamage) {
        BdLib.logWarn("%s requested with meta %d but specifies %d", source, meta, l.getItemDamage)
        l.setItemDamage(meta)
      }
      return l
    case x: Item =>
      return new ItemStack(x, cnt, meta)
    case x: Block =>
      return new ItemStack(x, cnt, meta)
    case _ =>
      error("%s is %s which cannot be translated to an ItemStack", source, x.getClass.getName)
  }

  /**
   * Fetches an ItemStack using reflection from static fields
   * Item and Block instances are converted to ItemStacks
   * @param clsName Name of class to fetch from (can be a class macro)
   * @param fldName Field name
   * @param meta Metadata or damage
   * @param cnt Stack size
   * @return Matching item stack
   */
  def reflectStack(clsName: String, fldName: String, meta: Int, cnt: Int): ItemStack = {
    val realName = getRealClassName(clsName)
    val fld = Class.forName(realName).getField(fldName).get(null)
    return sanitizeReflectedItem(fld, "%s.%s".format(realName, fldName), meta, cnt)
  }

  /**
   * Fetches an ItemStack using reflection from static getters
   * Item and Block instances are converted to ItemStacks
   * @param clsName Name of class to fetch from (can be a class macro)
   * @param method Method name
   * @param param Parameter to method
   * @param meta Metadata or damage
   * @param cnt Stack size
   * @return Matching item stack
   */
  def reflectStackGetter(clsName: String, method: String, param: String, meta: Int, cnt: Int): ItemStack = {
    var realName: String = clsName
    var realMethod: String = method

    //Some acrobatics to allow use with macros, even bare ones like $foo(bar)

    if ((realName.length == 0) && realMethod.startsWith("$")) {
      val v = getRealClassName(realMethod).split('.').reverse
      realName = v.tail.reverse.mkString(".")
      realMethod = v.head
    }
    if (realName.startsWith("$")) {
      realName = getRealClassName(clsName)
    }

    val res = Class.forName(realName).getMethod(realMethod, classOf[String]).invoke(null, param)
    return sanitizeReflectedItem(res, "%s.%s(%s)".format(realName, realMethod, param), meta, cnt)
  }

  /**
   * Returns all possible ItemStacks that match a reference
   * Currently everything except OreDictionary references just returns one item
   * @param s Parser ItemStack reference
   * @param cnt Stack size
   * @return List of matching ItemStacks
   */
  def getAllConcreteStacks(s: StackRef, cnt: Int = 1): Iterable[ItemStack] = s match {
    case StackOreDict(id) =>
      import scala.collection.JavaConversions._
      val c = OreDictionary.getOres(id).map(_.copy())
      c.foreach(_.stackSize = cnt)
      return c
    case _ => Seq(getConcreteStack(s))
  }

  def notNull[T](v: T, err: => String) = if (v == null) error(err) else v

  /**
   * Returns an ItemStack that match a reference
   * This is the main StackRef resolution method
   * @param s Parser ItemStack reference
   * @param cnt Stack size
   * @return A matching ItemStack
   */
  def getConcreteStack(s: StackRef, cnt: Int = 1): ItemStack = s match {
    case StackOreDict(id) =>
      val l = OreDictionary.getOres(id)
      if (l.size == 0) error("Concrete ItemStack requested for OD entry '%s' that is empty", id)
      val s = l.get(0).copy()
      s.stackSize = cnt
      BdLib.logDebug("Concrete ItemStack for OD entry '%s' -> %s", id, s)
      return s
    case StackMacro(ch) => getConcreteStack(currCharMap(ch), cnt)
    case StackGeneric(mod, id) =>
      notNull(GameRegistry.findItemStack(mod, id, cnt), "Stack not found %s:%s".format(mod, id))
    case StackBlock(mod, id, meta) =>
      val block = notNull(GameRegistry.findBlock(mod, id), "Block not found %s:%s".format(mod, id))
      if (Item.getItemFromBlock(block) == null)
        error("The block '%s:%s' does not have a valid matching item registered and thus can't be used in recipes", mod, id)
      new ItemStack(block, cnt, meta)
    case StackItem(mod, id, meta) =>
      new ItemStack(notNull(GameRegistry.findItem(mod, id), "Item not found %s:%s".format(mod, id)), cnt, meta)
    case StackReflect(cls, field, meta) => reflectStack(cls, field, meta, cnt)
    case StackGetter(cls, method, param, meta) => reflectStackGetter(cls, method, param, meta, cnt)
  }

  /**
   * Looks up all characters used in the recipe
   * @param s The pattern
   * @return Map from character to recipe components and a boolean that means OD-aware methods should be used
   */
  def resolveRecipeComponents(s: Iterable[Char]): (Map[Char, AnyRef], Boolean) = {
    var comp = Map.empty[Char, AnyRef]
    var needOd = false
    for (x <- s if !comp.contains(x) && x != '_') {
      if (!currCharMap.contains(x)) error("Character %s is undefined", x)
      val r = getRecipeComponent(currCharMap(x))
      if (r.isInstanceOf[String]) needOd = true
      BdLib.logDebug("%s -> %s", x, r)
      comp += (x -> r)
    }
    return (comp, needOd)
  }

  /**
   * Helper to remove recipes from lists
   * @param list list to check
   * @param res crafting result to remove
   * @return cleaned list
   */
  def clearStatements(list: List[RecipeStatement], res: StackRef): List[RecipeStatement] = {
    list flatMap {
      case x: CraftingStatement =>
        if (res == x.result) {
          BdLib.logDebug("Removing recipe %s", this)
          None
        } else Some(x)
      case RsRecipes(inner) =>
        Some(RsRecipes(clearStatements(inner, res)))
      case RsConditional(cnd, thn, els) =>
        Some(RsConditional(cnd, clearStatements(thn, res), clearStatements(els, res)))
      case x => Some(x)
    }
  }

  /**
   * Checks a condition and returns the result
   */
  def resolveCondition(cond: Condition): Boolean = cond match {
    case CndHaveMod(mod) =>
      Misc.haveModVersion(mod)
    case CndHaveAPI(api) =>
      ModAPIManager.INSTANCE.hasAPI(api)
    case CndHaveOD(od) =>
      OreDictionary.getOres(od).size() > 0
    case CndNOT(cnd) => !resolveCondition(cnd)
    case CndAND(c1, c2) => resolveCondition(c1) && resolveCondition(c2)
    case CndOR(c1, c2) => resolveCondition(c1) || resolveCondition(c2)
    case x =>
      error("Can't process %s - this is a programing bug!", x)
  }

  /**
   * Process a single statement, override this to add more statements
   * @param s The statement
   */
  def processConfigStatement(s: ConfigStatement): Unit = s match {
    case CsConditionalConfig(cnd, thn, els) =>
      if (resolveCondition(cnd)) {
        BdLib.logDebug("Condition %s - TRUE", cnd)
        processConfigStatementsSafe(thn)
      } else {
        BdLib.logDebug("Condition %s - FALSE", cnd)
        processConfigStatementsSafe(els)
      }

    case CsClearRecipes(res) =>
      BdLib.logDebug("Clearing recipes that produce %s", res)
      recipeStatements = clearStatements(recipeStatements, res)

    case CsRecipeBlock(lst) =>
      recipeStatements :+= RsRecipes(lst)

    case x =>
      BdLib.logError("Can't process %s - this is a programing bug!", x)
  }

  /**
   * Process a single recipe statement
   * @param st The statement
   */
  def processRecipeStatement(st: RecipeStatement) = st match {
    case RsCharAssign(c, r) =>
      currCharMap += (c -> r)
      BdLib.logDebug("Added %s = %s", c, r)

    case RsClassMacro(id, cls) =>
      currClassMacros += (id -> cls)
      BdLib.logDebug("Added def %s = %s", id, cls)

    case RsRecipeShaped(rec, res, cnt) =>
      BdLib.logDebug("Adding shaped recipe %s => %s * %d", rec, res, cnt)
      val (comp, needOd) = resolveRecipeComponents(rec.mkString(""))
      val resStack = getConcreteStack(res, cnt)

      if (resStack.getItemDamage == OreDictionary.WILDCARD_VALUE) {
        BdLib.logDebug("Result meta is unset, defaulting to 0")
        resStack.setItemDamage(0)
      }

      if (needOd)
        Misc.addRecipeOD(resStack, rec, comp)
      else
        Misc.addRecipe(resStack, rec, comp)

      BdLib.logDebug("Done... result=%s, od=%s", resStack, needOd)

    case RsRecipeShapeless(rec, res, cnt) =>
      BdLib.logDebug("Adding shapeless recipe %s => %s * %d", rec, res, cnt)
      val (comp, needOd) = resolveRecipeComponents(rec)
      val resStack = getConcreteStack(res, cnt)
      val recTrans = rec.toCharArray.map(comp(_))

      if (resStack.getItemDamage == OreDictionary.WILDCARD_VALUE) {
        BdLib.logDebug("Result meta is unset, defaulting to 0")
        resStack.setItemDamage(0)
      }

      if (needOd)
        GameRegistry.addRecipe(new ShapelessOreRecipe(resStack, recTrans: _*))
      else
        GameRegistry.addShapelessRecipe(resStack, recTrans: _*)

      BdLib.logDebug("Done... result=%s, od=%s", resStack, needOd)

    case RsRecipeSmelting(in, out, cnt, xp) =>
      BdLib.logDebug("Adding smelting recipe %s => %s * %d (%f xp)", in, out, cnt, xp)
      val outStack = getConcreteStack(out, cnt)
      if (outStack.getItemDamage == OreDictionary.WILDCARD_VALUE) {
        BdLib.logDebug("Result meta is unset, defaulting to 0")
        outStack.setItemDamage(0)
      }
      for (inStack <- getAllConcreteStacks(in, 1)) {
        GameRegistry.addSmelting(inStack, outStack, xp)
        BdLib.logDebug("added %s -> %s", inStack, outStack)
      }

    case RsConditional(cnd, thn, els) =>
      if (resolveCondition(cnd)) {
        BdLib.logDebug("Condition %s - TRUE", cnd)
        processRecipeStatementsInSubcontext(thn)
      } else {
        BdLib.logDebug("Condition %s - FALSE", cnd)
        processRecipeStatementsInSubcontext(els)
      }

    case RsRecipes(list) =>
      processRecipeStatementsInSubcontext(list)

    case RsRegOredict(id, spec, wildcard) =>
      BdLib.logDebug("Registering ore dictionary entry: %s -> %s", spec, id)
      val stack = getConcreteStack(spec)
      if (wildcard) {
        BdLib.logDebug("Forcing wildcard damage (was %d)", stack.getItemDamage)
        stack.setItemDamage(OreDictionary.WILDCARD_VALUE)
      }
      BdLib.logDebug("Actual stack: %s", stack)
      OreDictionary.registerOre(id, stack)

    case x =>
      BdLib.logError("Can't process %s - this is a programing bug!", x)
  }

  /**
   * Processes recipe statements in a new context
   * Any changes to mutable state will not persist when this method returns
   */
  def processRecipeStatementsInSubcontext(list: List[RecipeStatement]) = {
    val oldCharMap = currCharMap
    val oldClassMacros = currClassMacros
    try {
      processRecipeStatementsSafe(list)
    } finally {
      currCharMap = oldCharMap
      currClassMacros = oldClassMacros
    }
  }

  /**
   * Process main recipe statements list, clear the list afterwards
   */
  def processRecipeStatements() {
    BdLib.logDebug("Processing %d recipe statements", recipeStatements.size)
    processRecipeStatementsSafe(recipeStatements)
    recipeStatements = List.empty
  }

  /**
   * Process a list of recipe statements and catch all exceptions
   * @param list The list to process
   */
  def processRecipeStatementsSafe(list: List[RecipeStatement]) {
    for (s <- list) {
      try {
        processRecipeStatement(s)
      } catch {
        case e: StatementError =>
          BdLib.logError("Error while processing %s: %s", s, e.getMessage)
        case e: Throwable =>
          BdLib.logErrorException("Error while processing %s", e, s)
      }
    }
  }

  /**
   * Process a list of config statements and catch all exceptions
   * @param r The list to process
   */
  def processConfigStatementsSafe(r: List[ConfigStatement]): Unit = {
    for (s <- r) {
      try {
        processConfigStatement(s)
      } catch {
        case e: StatementError =>
          BdLib.logError("Error while processing %s: %s", s, e.getMessage)
        case e: Throwable =>
          BdLib.logErrorException("Error while processing %s", e, s)
      }
    }
  }

  def load(f: Reader) {
    BdLib.logDebug("Starting parsing")
    val r = newParser().doParse(f)
    BdLib.logDebug("Processing %d statements", r.size)
    processConfigStatementsSafe(r)
    BdLib.logDebug("Done")
  }
}
