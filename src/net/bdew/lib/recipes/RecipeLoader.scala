/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.recipes

import java.io.Reader
import net.minecraft.item.{Item, ItemStack}
import net.bdew.lib.{BdLib, Misc}
import net.minecraft.block.Block
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.{ShapelessOreRecipe, OreDictionary}
import java.util.logging.Logger
import net.minecraft.item.crafting.FurnaceRecipes

/**
 * Main recipe loader class
 * The input file is parsed by [[net.bdew.lib.recipes.RecipeParser]]
 * Into a list of [[net.bdew.lib.recipes.Statement]] subclasses
 * That are then executed here
 */

class RecipeLoader {

  /**
   * Create a new parser, override in subclasses to use an extended parser
   * @return New ConfigParser (or subclass) instance
   */
  def newParser() = new RecipeParser()

  val log: Logger = Logger.getLogger("RecipeLoader")
  log.setParent(BdLib.log)

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
   * List of unprocessed delayed statements
   */
  var delayedStatements = List.empty[DelayedStatement]

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
  def getRealClassName(s: String): String = {
    if (s.startsWith("$")) {
      return currClassMacros(s.stripPrefix("$"))
    }
    return s
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
        log.warning("%s requested with meta %d but specifies %d".format(source, meta, l.getItemDamage))
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
   * Item and Block instaces are converted to ItemStacks
   * @param clsName Name of class to fetch from (can be a class macro)
   * @param fldName Field name
   * @param meta Metadata or damage
   * @param cnt Stack size
   * @return Matching itemstack
   */
  def reflectStack(clsName: String, fldName: String, meta: Int, cnt: Int): ItemStack = {
    val realName = getRealClassName(clsName)
    val cls = Class.forName(realName)
    val fld = cls.getField(fldName).get(null)
    return sanitizeReflectedItem(fld, "%s.%s".format(realName, fldName), meta, cnt)
  }

  /**
   * Fetches an ItemStack using reflection from static getters
   * Item and Block instaces are converted to ItemStacks
   * @param clsName Name of class to fetch from (can be a class macro)
   * @param method Method name
   * @param param Parameter to method
   * @param meta Metadata or damage
   * @param cnt Stack size
   * @return Matching itemstack
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

    val cls = Class.forName(realName)
    val meth = cls.getMethod(realMethod, classOf[String])
    val res = meth.invoke(null, param)
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
      log.info("Concrete ItemStack for OD entry '%s' -> %s".format(id, s))
      return s
    case StackMacro(ch) => getConcreteStack(currCharMap(ch), cnt)
    case StackMod(mod, id) =>
      val i = GameRegistry.findItemStack(mod, id, cnt)
      if (i == null) error("Item %s:%s not found", mod, id)
      i
    case StackMCBlock(id, meta) => new ItemStack(Vanilla.blocks(id), cnt, meta)
    case StackMCItem(id, meta) => new ItemStack(Vanilla.items(id), cnt, meta)
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
      log.info("%s -> %s".format(x, r))
      comp += (x -> r)
    }
    return (comp, needOd)
  }

  /**
   * Process a single statement, override this to add more statements
   * @param s The statement
   */
  def processStatement(s: Statement): Unit = s match {
    case StIfHaveMod(mod, thn, els) =>
      if (Misc.haveModVersion(mod)) {
        log.info("ifMod: %s found".format(mod))
        processStatementsSafe(thn)
      } else {
        log.info("ifMod: %s not found".format(mod))
        processStatementsSafe(els)
      }

    case StIfHaveOD(od, thn, els) =>
      if (OreDictionary.getOres(od).size() > 0) {
        log.info("ifOreDict: %s found".format(od))
        processStatementsSafe(thn)
      } else {
        log.info("ifOreDict: %s not found".format(od))
        processStatementsSafe(els)
      }

    case StClearRecipes(res) =>
      log.info("Clearing recipes that produce %s".format(res))
      delayedStatements = delayedStatements.filter({
        x =>
          if (x.isInstanceOf[CraftingStatement])
            if (x.asInstanceOf[CraftingStatement].result == res) {
              log.info("Removing recipe %s".format(x))
              false
            } else true
          else true
      })

    case x: DelayedStatement =>
      delayedStatements :+= x

    case x =>
      log.severe("Can't process %s - this is a programing bug!".format(x))
  }

  /**
   * Process a single delayed statement
   * @param st The statement
   */
  def processDelayedStatement(st: DelayedStatement) = st match {
    case StCharAssign(c, r) =>
      currCharMap += (c -> r)
      log.info("Added %s = %s".format(c, r))

    case StClassMacro(id, cls) =>
      currClassMacros += (id -> cls)
      log.info("Added def %s = %s".format(id, cls))

    case StRecipeShaped(rec, res, cnt) =>
      log.info("Adding shaped recipe %s => %s * %d".format(rec, res, cnt))
      val (comp, needOd) = resolveRecipeComponents(rec.mkString(""))
      val resStack = getConcreteStack(res, cnt)

      if (resStack.getItemDamage == OreDictionary.WILDCARD_VALUE) {
        log.info("Result meta is unset, defaulting to 0")
        resStack.setItemDamage(0)
      }

      if (needOd)
        Misc.addRecipeOD(resStack, rec, comp)
      else
        Misc.addRecipe(resStack, rec, comp)

      log.info("Done... result=%s, od=%s".format(resStack, needOd))

    case StRecipeShapeless(rec, res, cnt) =>
      log.info("Adding shapeless recipe %s => %s * %d".format(rec, res, cnt))
      val (comp, needOd) = resolveRecipeComponents(rec)
      val resStack = getConcreteStack(res, cnt)
      val recTrans = rec.toCharArray.map(comp(_))

      if (resStack.getItemDamage == OreDictionary.WILDCARD_VALUE) {
        log.info("Result meta is unset, defaulting to 0")
        resStack.setItemDamage(0)
      }

      if (needOd)
        GameRegistry.addRecipe(new ShapelessOreRecipe(resStack, recTrans: _*))
      else
        GameRegistry.addShapelessRecipe(resStack, recTrans: _*)

      log.info("Done... result=%s, od=%s".format(resStack, needOd))

    case StSmeltRecipe(in, out, cnt, xp) =>
      log.info("Adding smelting recipe %s => %s * %d (%f xp)".format(in, out, cnt, xp))
      val outStack = getConcreteStack(out, cnt)
      if (outStack.getItemDamage == OreDictionary.WILDCARD_VALUE) {
        log.info("Result meta is unset, defaulting to 0")
        outStack.setItemDamage(0)
      }
      for (inStack <- getAllConcreteStacks(in, 1)) {
        if (inStack.getItemDamage == OreDictionary.WILDCARD_VALUE) {
          FurnaceRecipes.smelting.addSmelting(inStack.itemID, outStack, xp)
          log.info("added %d -> %s".format(inStack.itemID, outStack))
        } else {
          FurnaceRecipes.smelting.addSmelting(inStack.itemID, inStack.getItemDamage, outStack, xp)
          log.info("added %d@%d -> %s".format(inStack.itemID, inStack.getItemDamage, outStack))
        }
      }

    case x =>
      log.severe("Can't process %s - this is a programing bug!".format(x))
  }

  /**
   * Process delayed statements, clear the list afterwards
   */
  def processDelayedStatements() {
    log.info("Processing %d delayed statements".format(delayedStatements.size))
    for (s <- delayedStatements) {
      try {
        processDelayedStatement(s)
      } catch {
        case e: StatementError =>
          log.severe("Error while processing %s: %s".format(s, e.getMessage))
        case e: Throwable =>
          log.severe("Error while processing %s: %s".format(s, e))
          e.printStackTrace()
      }
    }
    delayedStatements = List.empty
  }

  /**
   * Process a list of statements and catch all exceptions
   * @param r The list to process
   */
  def processStatementsSafe(r: List[Statement]): Unit = {
    for (s <- r) {
      try {
        processStatement(s)
      } catch {
        case e: StatementError =>
          log.severe("Error while processing %s: %s".format(s, e.getMessage))
        case e: Throwable =>
          log.severe("Error while processing %s: %s".format(s, e))
          e.printStackTrace()
      }
    }
  }

  def load(f: Reader) {
    log.info("Starting parsing")
    val r = newParser().doParse(f)
    log.info("Processing %d statements".format(r.size))
    processStatementsSafe(r)
    log.info("Done")
  }
}
