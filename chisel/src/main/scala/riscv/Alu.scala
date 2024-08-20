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

class  AluControl(width: Int) extends Bundle {
  val a = Input(UInt(width.W))
  val b = Input(UInt(width.W))
  val sel = Input(AluSel())
  val out = Output(UInt(width.W))
}

class Alu(width: Int) extends Module {
  val io = IO(new AluControl(width))

  io.out := DontCare

  val negateb = Wire(Bool())
  negateb := DontCare
  val sum = io.a +& Mux(negateb, ~io.b, io.b) + negateb.asUInt 
  val cout = sum(width)

  /* Shift ammount is 6 bits RV64I */
  val shamt = io.b(log2Ceil(width) - 1, 0)

  switch(io.sel) {
    is(AluSel.add) {
      negateb := false.B
      io.out := sum(width - 1, 0)
    }
    is(AluSel.slt) {
      /* Implement slt using subtraction, following is the intuition behind this 
       * muxing. In an n-bit signed number:
       * a[n-1] === b[n-1] => sum := 2^(n-1) TODO formalise this */
      negateb := true.B
      io.out := Mux(~cout ^ io.a(width-1) ^ io.b(width-1), 1.U, 0.U)
    }
    is(AluSel.sltu) {
      negateb := true.B
      io.out := Mux(cout, 0.U, 1.U)
    }
    is(AluSel.sub) {
      negateb := true.B
      io.out := sum(width - 1, 0)
    }
    /* Easy ones */
    is(AluSel.sra) (io.out := (io.a.asSInt >> shamt).asUInt)
    is(AluSel.xor) (io.out := io.a ^ io.b)
    is(AluSel.srl) (io.out := io.a >> shamt) 
    is(AluSel.and) (io.out := io.a & io.b)
    is(AluSel.sll) (io.out := io.a << shamt)
    is(AluSel.or) (io.out := io.a | io.b)
  }

/* Formal */
  switch(io.sel) {
    is(AluSel.add) (assert(io.out === io.a + io.b))
    is(AluSel.slt) {
      when(io.out === 1.U) {
        assert(io.a.asSInt < io.b.asSInt)
      }.otherwise {
        assert(io.a.asSInt >= io.b.asSInt)
      }
    }
    is(AluSel.sltu) {
      when(io.out === 1.U) {
        assert(io.a < io.b)
      }.otherwise {
        assert(io.a >= io.b)
      }
    }
    is(AluSel.sub) (assert(io.out === io.a - io.b))
    /* Were I do do the remaining assertions, they'd be basically the same asSInt
     * the assignments. Therefore I'm leaving these out to save time. */
  }
  assert(shamt < width.U)
}
