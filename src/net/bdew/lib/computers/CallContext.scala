/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.computers

import net.minecraft.tileentity.TileEntity

case class CallContext[T <: TileEntity](tile: T, rawParams: Array[AnyRef]) {
  private def optionalize(a: Array[AnyRef], n: Int) =
    (for (i <- 0 until Math.max(a.length, n)) yield {
      if (i >= a.length)
        None
      else
        Option(a(i))
    }).toArray

  // In good scala tradition (see Function, Tuple, etc.) this has to be spelled
  // out manually for every possible number of parameters
  // No parrots were hurt in the making of this class

  def params[P1](p1: CallParam[P1]) =
    optionalize(rawParams, 1) match {
      case Array(p1(r1)) => r1
      case _ => throw ParameterErrorException(p1)
    }

  def params[P1, P2](p1: CallParam[P1], p2: CallParam[P2]) =
    optionalize(rawParams, 2) match {
      case Array(p1(r1), p2(r2)) => (r1, r2)
      case _ => throw ParameterErrorException(p1, p2)
    }

  def params[P1, P2, P3](p1: CallParam[P1], p2: CallParam[P2], p3: CallParam[P3]) =
    optionalize(rawParams, 3) match {
      case Array(p1(r1), p2(r2), p3(r3)) => (r1, r2, r3)
      case _ => throw ParameterErrorException(p1, p2, p3)
    }

  def params[P1, P2, P3, P4](p1: CallParam[P1], p2: CallParam[P2], p3: CallParam[P3], p4: CallParam[P4]) =
    optionalize(rawParams, 4) match {
      case Array(p1(r1), p2(r2), p3(r3), p4(r4)) => (r1, r2, r3, r4)
      case _ => throw ParameterErrorException(p1, p2, p3, p4)
    }

  def params[P1, P2, P3, P4, P5](p1: CallParam[P1], p2: CallParam[P2], p3: CallParam[P3], p4: CallParam[P4], p5: CallParam[P5]) =
    optionalize(rawParams, 5) match {
      case Array(p1(r1), p2(r2), p3(r3), p4(r4), p5(r5)) => (r1, r2, r3, r4, r5)
      case _ => throw ParameterErrorException(p1, p2, p3, p4, p5)
    }

  def params[P1, P2, P3, P4, P5, P6](p1: CallParam[P1], p2: CallParam[P2], p3: CallParam[P3], p4: CallParam[P4], p5: CallParam[P5], p6: CallParam[P6]) =
    optionalize(rawParams, 6) match {
      case Array(p1(r1), p2(r2), p3(r3), p4(r4), p5(r5), p6(r6)) => (r1, r2, r3, r4, r5, r6)
      case _ => throw ParameterErrorException(p1, p2, p3, p4, p5, p6)
    }

  def params[P1, P2, P3, P4, P5, P6, P7](p1: CallParam[P1], p2: CallParam[P2], p3: CallParam[P3], p4: CallParam[P4], p5: CallParam[P5], p6: CallParam[P6], p7: CallParam[P7]) =
    optionalize(rawParams, 7) match {
      case Array(p1(r1), p2(r2), p3(r3), p4(r4), p5(r5), p6(r6), p7(r7)) => (r1, r2, r3, r4, r5, r6, r7)
      case _ => throw ParameterErrorException(p1, p2, p3, p4, p5, p6, p7)
    }

  def params[P1, P2, P3, P4, P5, P6, P7, P8](p1: CallParam[P1], p2: CallParam[P2], p3: CallParam[P3], p4: CallParam[P4], p5: CallParam[P5], p6: CallParam[P6], p7: CallParam[P7], p8: CallParam[P8]) =
    optionalize(rawParams, 8) match {
      case Array(p1(r1), p2(r2), p3(r3), p4(r4), p5(r5), p6(r6), p7(r7), p8(r8)) => (r1, r2, r3, r4, r5, r6, r7, r8)
      case _ => throw ParameterErrorException(p1, p2, p3, p4, p5, p6, p7, p8)
    }

  def params[P1, P2, P3, P4, P5, P6, P7, P8, P9](p1: CallParam[P1], p2: CallParam[P2], p3: CallParam[P3], p4: CallParam[P4], p5: CallParam[P5], p6: CallParam[P6], p7: CallParam[P7], p8: CallParam[P8], p9: CallParam[P9]) =
    optionalize(rawParams, 9) match {
      case Array(p1(r1), p2(r2), p3(r3), p4(r4), p5(r5), p6(r6), p7(r7), p8(r8), p9(r9)) => (r1, r2, r3, r4, r5, r6, r7, r8, r9)
      case _ => throw ParameterErrorException(p1, p2, p3, p4, p5, p6, p7, p8, p9)
    }

  def params[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10](p1: CallParam[P1], p2: CallParam[P2], p3: CallParam[P3], p4: CallParam[P4], p5: CallParam[P5], p6: CallParam[P6], p7: CallParam[P7], p8: CallParam[P8], p9: CallParam[P9], p10: CallParam[P10]) =
    optionalize(rawParams, 10) match {
      case Array(p1(r1), p2(r2), p3(r3), p4(r4), p5(r5), p6(r6), p7(r7), p8(r8), p9(r9), p10(r10)) => (r1, r2, r3, r4, r5, r6, r7, r8, r9, r10)
      case _ => throw ParameterErrorException(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10)
    }

  def params[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11](p1: CallParam[P1], p2: CallParam[P2], p3: CallParam[P3], p4: CallParam[P4], p5: CallParam[P5], p6: CallParam[P6], p7: CallParam[P7], p8: CallParam[P8], p9: CallParam[P9], p10: CallParam[P10], p11: CallParam[P11]) =
    optionalize(rawParams, 11) match {
      case Array(p1(r1), p2(r2), p3(r3), p4(r4), p5(r5), p6(r6), p7(r7), p8(r8), p9(r9), p10(r10), p11(r11)) => (r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11)
      case _ => throw ParameterErrorException(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11)
    }

  def params[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12](p1: CallParam[P1], p2: CallParam[P2], p3: CallParam[P3], p4: CallParam[P4], p5: CallParam[P5], p6: CallParam[P6], p7: CallParam[P7], p8: CallParam[P8], p9: CallParam[P9], p10: CallParam[P10], p11: CallParam[P11], p12: CallParam[P12]) =
    optionalize(rawParams, 12) match {
      case Array(p1(r1), p2(r2), p3(r3), p4(r4), p5(r5), p6(r6), p7(r7), p8(r8), p9(r9), p10(r10), p11(r11), p12(r12)) => (r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12)
      case _ => throw ParameterErrorException(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12)
    }

  def params[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13](p1: CallParam[P1], p2: CallParam[P2], p3: CallParam[P3], p4: CallParam[P4], p5: CallParam[P5], p6: CallParam[P6], p7: CallParam[P7], p8: CallParam[P8], p9: CallParam[P9], p10: CallParam[P10], p11: CallParam[P11], p12: CallParam[P12], p13: CallParam[P13]) =
    optionalize(rawParams, 13) match {
      case Array(p1(r1), p2(r2), p3(r3), p4(r4), p5(r5), p6(r6), p7(r7), p8(r8), p9(r9), p10(r10), p11(r11), p12(r12), p13(r13)) => (r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13)
      case _ => throw ParameterErrorException(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13)
    }

  def params[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14](p1: CallParam[P1], p2: CallParam[P2], p3: CallParam[P3], p4: CallParam[P4], p5: CallParam[P5], p6: CallParam[P6], p7: CallParam[P7], p8: CallParam[P8], p9: CallParam[P9], p10: CallParam[P10], p11: CallParam[P11], p12: CallParam[P12], p13: CallParam[P13], p14: CallParam[P14]) =
    optionalize(rawParams, 14) match {
      case Array(p1(r1), p2(r2), p3(r3), p4(r4), p5(r5), p6(r6), p7(r7), p8(r8), p9(r9), p10(r10), p11(r11), p12(r12), p13(r13), p14(r14)) => (r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14)
      case _ => throw ParameterErrorException(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14)
    }

  def params[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15](p1: CallParam[P1], p2: CallParam[P2], p3: CallParam[P3], p4: CallParam[P4], p5: CallParam[P5], p6: CallParam[P6], p7: CallParam[P7], p8: CallParam[P8], p9: CallParam[P9], p10: CallParam[P10], p11: CallParam[P11], p12: CallParam[P12], p13: CallParam[P13], p14: CallParam[P14], p15: CallParam[P15]) =
    optionalize(rawParams, 15) match {
      case Array(p1(r1), p2(r2), p3(r3), p4(r4), p5(r5), p6(r6), p7(r7), p8(r8), p9(r9), p10(r10), p11(r11), p12(r12), p13(r13), p14(r14), p15(r15)) => (r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15)
      case _ => throw ParameterErrorException(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15)
    }

  def params[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16](p1: CallParam[P1], p2: CallParam[P2], p3: CallParam[P3], p4: CallParam[P4], p5: CallParam[P5], p6: CallParam[P6], p7: CallParam[P7], p8: CallParam[P8], p9: CallParam[P9], p10: CallParam[P10], p11: CallParam[P11], p12: CallParam[P12], p13: CallParam[P13], p14: CallParam[P14], p15: CallParam[P15], p16: CallParam[P16]) =
    optionalize(rawParams, 16) match {
      case Array(p1(r1), p2(r2), p3(r3), p4(r4), p5(r5), p6(r6), p7(r7), p8(r8), p9(r9), p10(r10), p11(r11), p12(r12), p13(r13), p14(r14), p15(r15), p16(r16)) => (r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16)
      case _ => throw ParameterErrorException(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16)
    }

  def params[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17](p1: CallParam[P1], p2: CallParam[P2], p3: CallParam[P3], p4: CallParam[P4], p5: CallParam[P5], p6: CallParam[P6], p7: CallParam[P7], p8: CallParam[P8], p9: CallParam[P9], p10: CallParam[P10], p11: CallParam[P11], p12: CallParam[P12], p13: CallParam[P13], p14: CallParam[P14], p15: CallParam[P15], p16: CallParam[P16], p17: CallParam[P17]) =
    optionalize(rawParams, 17) match {
      case Array(p1(r1), p2(r2), p3(r3), p4(r4), p5(r5), p6(r6), p7(r7), p8(r8), p9(r9), p10(r10), p11(r11), p12(r12), p13(r13), p14(r14), p15(r15), p16(r16), p17(r17)) => (r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17)
      case _ => throw ParameterErrorException(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17)
    }

  def params[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18](p1: CallParam[P1], p2: CallParam[P2], p3: CallParam[P3], p4: CallParam[P4], p5: CallParam[P5], p6: CallParam[P6], p7: CallParam[P7], p8: CallParam[P8], p9: CallParam[P9], p10: CallParam[P10], p11: CallParam[P11], p12: CallParam[P12], p13: CallParam[P13], p14: CallParam[P14], p15: CallParam[P15], p16: CallParam[P16], p17: CallParam[P17], p18: CallParam[P18]) =
    optionalize(rawParams, 18) match {
      case Array(p1(r1), p2(r2), p3(r3), p4(r4), p5(r5), p6(r6), p7(r7), p8(r8), p9(r9), p10(r10), p11(r11), p12(r12), p13(r13), p14(r14), p15(r15), p16(r16), p17(r17), p18(r18)) => (r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18)
      case _ => throw ParameterErrorException(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18)
    }

  def params[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19](p1: CallParam[P1], p2: CallParam[P2], p3: CallParam[P3], p4: CallParam[P4], p5: CallParam[P5], p6: CallParam[P6], p7: CallParam[P7], p8: CallParam[P8], p9: CallParam[P9], p10: CallParam[P10], p11: CallParam[P11], p12: CallParam[P12], p13: CallParam[P13], p14: CallParam[P14], p15: CallParam[P15], p16: CallParam[P16], p17: CallParam[P17], p18: CallParam[P18], p19: CallParam[P19]) =
    optionalize(rawParams, 19) match {
      case Array(p1(r1), p2(r2), p3(r3), p4(r4), p5(r5), p6(r6), p7(r7), p8(r8), p9(r9), p10(r10), p11(r11), p12(r12), p13(r13), p14(r14), p15(r15), p16(r16), p17(r17), p18(r18), p19(r19)) => (r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19)
      case _ => throw ParameterErrorException(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19)
    }

  def params[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20](p1: CallParam[P1], p2: CallParam[P2], p3: CallParam[P3], p4: CallParam[P4], p5: CallParam[P5], p6: CallParam[P6], p7: CallParam[P7], p8: CallParam[P8], p9: CallParam[P9], p10: CallParam[P10], p11: CallParam[P11], p12: CallParam[P12], p13: CallParam[P13], p14: CallParam[P14], p15: CallParam[P15], p16: CallParam[P16], p17: CallParam[P17], p18: CallParam[P18], p19: CallParam[P19], p20: CallParam[P20]) =
    optionalize(rawParams, 20) match {
      case Array(p1(r1), p2(r2), p3(r3), p4(r4), p5(r5), p6(r6), p7(r7), p8(r8), p9(r9), p10(r10), p11(r11), p12(r12), p13(r13), p14(r14), p15(r15), p16(r16), p17(r17), p18(r18), p19(r19), p20(r20)) => (r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20)
      case _ => throw ParameterErrorException(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20)
    }
}