/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.crafting

import net.minecraft.block.Block
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.{Item, ItemStack}

/**
  * Support class from custom recipes, implements crafting inventory search/matching
  */
class RecipeMatcher(inv: InventoryCrafting) {

  /**
    * Represents an ItemStack in the crafting inventory
    */
  case class Entry(x: Int, y: Int, stack: ItemStack) {
    /**
      * @return a search rule that matches items above this stack
      */
    def matchAbove = at(x, y - 1)

    /**
      * @return a search rule that matches items below this stack
      */
    def matchBelow = at(x, y + 1)

    /**
      * @return a search rule that matches items to the right of this stack
      */
    def matchRight = at(x + 1, y)

    /**
      * @return a search rule that matches items to the left of this stack
      */
    def matchLeft = at(x - 1, y)

    def matchAdjacent = matchAbove or matchBelow or matchLeft or matchRight

    /**
      * @return a search rule that matches the same stack
      */
    def same = Rule(that => ItemStack.areItemStackTagsEqual(this.stack, that.stack))
  }

  /**
    * Represents a search rule
    *
    * @param f a function that returns true if the entry matches this rule
    */
  case class Rule(f: (Entry) => Boolean) {
    /**
      * @return A new rule that will match if both this and another rule match
      */
    def and(r2: Rule) = Rule(m => f(m) && r2.f(m))

    /**
      * @return A new rule that will match if either this or another rule match (or both)
      */
    def or(r2: Rule) = Rule(m => f(m) || r2.f(m))

    /**
      * @return A new rule that will match if either this or another rule match (but not both)
      */
    def xor(r2: Rule) = Rule(m => f(m) ^ r2.f(m))

    /**
      * Looks up the first entry matching this rule that was not matched earlier and marks it as matched
      *
      * @return The first entry matching this rule that was not matched earlier, or None if nothing matched
      */
    def first() = items find f flatMap mark

    /**
      * Looks up all entries matching this rule that were not matched earlier and marks them as matched
      *
      * @return All entries matching this rule that were not matched earlier
      */
    def all() = items filter f flatMap mark

    /**
      * Verifies that the rule matches at-least one item that was not matched earlier and marks all as matched
      *
      * @return true if any items match
      */
    def verify() = all().nonEmpty
  }

  /**
    * Set of all the stacks in the crafting inventory
    */
  val items = for (i <- 0 until inv.getWidth; j <- 0 until inv.getHeight; stack <- Option(inv.getStackInRowAndColumn(i, j)) if !stack.isEmpty) yield Entry(i, j, stack)

  val itemSet = items.toSet

  /**
    * Set of stacks that were matched by a rule
    */
  var matched = Set.empty[Entry]

  /**
    * @return Set of stacks that were not matched by a rule
    */
  def unmatched = itemSet -- matched

  /**
    * @return True if all stacks have been matched by rules
    */
  def allMatched = matched == itemSet

  private def mark(e: Entry) =
    if (matched contains e) {
      None
    } else {
      matched += e
      Some(e)
    }

  /**
    * @return A rule that matches a specific position
    */
  def at(x: Int, y: Int) = Rule(e => e.x == x && e.y == y)

  /**
    * @return A rule that matches a specific item
    */
  def matchItem(item: Item) = Rule(e => e.stack.getItem == item)

  /**
    * @return A rule that matches a specific item and damage
    */
  def matchItem(item: Item, damage: Int) = Rule(e => e.stack.getItem == item && e.stack.getItemDamage == damage)

  /**
    * @return A rule that matches items using a function
    */
  def matchItem(f: (Item) => Boolean) = Rule(x => f(x.stack.getItem))

  /**
    * @return A rule that matches a specific block
    */
  def matchBlock(block: Block) = Rule(e => Option(Block.getBlockFromItem(e.stack.getItem)) contains block)

  /**
    * @return A rule that matches a specific block and damage
    */
  def matchBlock(block: Block, damage: Int) = Rule(e => (Option(Block.getBlockFromItem(e.stack.getItem)) contains block) && e.stack.getItemDamage == damage)

  /**
    * @return A rule that matches blocks using a function
    */
  def matchBlock(f: (Block) => Boolean) = Rule(e => Option(Block.getBlockFromItem(e.stack.getItem)) exists f)

  /**
    * @return A rule that matches ItemStacks using a function
    */
  def matchStack(f: (ItemStack) => Boolean) = Rule(x => f(x.stack))

}
