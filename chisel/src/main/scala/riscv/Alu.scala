package riscv.alu

import chisel3._
import chisel3.util._
import chiseltest.formal._

object AluSel extends ChiselEnum {
  val add = Value("b0000".U)
  val sll = Value("b0001".U)
  val slt = Value("b0010".U)
  val sltu = Value("b0011".U)
  val xor = Value("b0100".U)
  val srl = Value("b0101".U)
  val or = Value("b0110".U)
  val and = Value("b0111".U)
  val sub = Value("b1000".U)
  val sra = Value("b1101".U)
}

class AluControl(width: Int) extends Bundle {
  val a = Input(UInt(width.W))
  val b = Input(UInt(width.W))
  val sel = Input(AluSel())
}

class AluOut(width: Int) extends Bundle {
  val out = Output(UInt(width.W))
}

class Alu(width: Int) extends Module {
  val in = IO(new AluControl(width))
  val out = IO(new AluOut(width))

  out.out := DontCare

  val negateb = Wire(Bool())
  negateb := DontCare
  val sum = in.a +& Mux(negateb, ~in.b, in.b) + negateb.asUInt
  val cout = sum(width)

  /* Shift ammount is 6 bits RV64I */
  val shamt = in.b(log2Ceil(width) - 1, 0)

  switch(in.sel) {
    is(AluSel.add) {
      negateb := false.B
      out.out := sum(width - 1, 0)
    }
    is(AluSel.slt) {
      /* Implement slt using subtraction, following is the intuition behind this 
       * muxing. In an n-bit signed number:
       * a[n-1] === b[n-1] => sum := 2^(n-1) TODO formalise this */
      negateb := true.B
      out.out := Mux(~cout ^ in.a(width-1) ^ in.b(width-1), 1.U, 0.U)
    }
    is(AluSel.sltu) {
      negateb := true.B
      out.out := Mux(cout, 0.U, 1.U)
    }
    is(AluSel.sub) {
      negateb := true.B
      out.out := sum(width - 1, 0)
    }
    /* Easy ones */
    is(AluSel.sra) (out.out := (in.a.asSInt >> shamt).asUInt)
    is(AluSel.xor) (out.out := in.a ^ in.b)
    is(AluSel.srl) (out.out := in.a >> shamt)
    is(AluSel.and) (out.out := in.a & in.b)
    is(AluSel.sll) (out.out := in.a << shamt)
    is(AluSel.or) (out.out := in.a | in.b)
  }

/* Formal */
  switch(in.sel) {
    is(AluSel.add) (assert(out.out === in.a + in.b))
    is(AluSel.slt) {
      when(out.out === 1.U) {
        assert(in.a.asSInt < in.b.asSInt)
      }.otherwise {
        assert(in.a.asSInt >= in.b.asSInt)
      }
    }
    is(AluSel.sltu) {
      when(out.out === 1.U) {
        assert(in.a < in.b)
      }.otherwise {
        assert(in.a >= in.b)
      }
    }
    is(AluSel.sub) (assert(out.out === in.a - in.b))
    /* Were I do do the remaining assertions, they'd be basically the same asSInt
     * the assignments. Therefore I'm leaving these out to save time. */
  }
  assert(shamt < width.U)
}
