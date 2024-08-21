package riscv.regfile

import chisel3._
import chisel3.util._
import chiseltest.formal._

class RegControl(numregs: Int, width: Int) extends Bundle {
  val selectWidth = log2Ceil(numregs).W
  val s1 = Input(UInt(selectWidth))
  val s2 = Input(UInt(selectWidth))
  val sd = Input(UInt(selectWidth))
  val din = Input(UInt(width.W))
  val wen = Input(Bool())
}

class RegOut(width: Int) extends Bundle {
  val rs1 = Output(UInt(width.W))
  val rs2 = Output(UInt(width.W))
}

class Regfile(numregs: Int, width: Int) extends Module {
  val in = IO(new RegControl(numregs, width))
  val out = IO(new RegOut(width))

  val regs = RegInit(VecInit(Seq.fill(numregs)(0.U(width.W))))

  out.rs1 := regs(in.s1)
  out.rs2 := regs(in.s2)

  when(in.sd =/= 0.U && in.wen) {
    regs(in.sd) := in.din
    /* Perform register file forwarding */
    when(in.s1 === in.sd) {
      out.rs1 := in.din
    }
    when(in.s2 === in.sd) {
      out.rs2 := in.din
    }
  }

/* Formal */
  assert(regs(0) === 0.U)

  when(in.s1 === 0.U) {
    assert(out.rs1 === 0.U)
  }.otherwise {
    when(in.s1 === in.sd && in.wen) {
      assert(out.rs1 === in.din)
    }
  }

  when(in.s2 === 0.U) {
    assert(out.rs2 === 0.U)
  }.otherwise {
    when(in.s2 === in.sd && in.wen) {
      assert(out.rs2 === in.din)
    }
  }

  when(past(in.sd =/= 0.U && in.wen)) {
    assert(regs(past(in.sd)) === past(in.din))
  }
}
