package riscv.stage.reg.regfile

import chisel3._
import chisel3.util._
import chiseltest.formal._

class RegIo(numregs: Int, width: Int) extends Bundle {
  val selectWidth = log2Ceil(numregs).W
  val s1 = Input(UInt(selectWidth))
  val s2 = Input(UInt(selectWidth))
  val sd = Input(UInt(selectWidth))
  val din = Input(UInt(width.W))
  val wen = Input(Bool())
  val rs1 = Output(UInt(width.W))
  val rs2 = Output(UInt(width.W))
}

class Regfile(numregs: Int, width: Int) extends Module {
  val io = IO(new RegIo(numregs, width))

  val regs = RegInit(VecInit(Seq.fill(numregs)(0.U(width.W))))

  io.rs1 := regs(io.s1)
  io.rs2 := regs(io.s2)

  when(io.sd =/= 0.U && io.wen) {
    regs(io.sd) := io.din
    /* Perform register file forwarding */
    when(io.s1 === io.sd) {
      io.rs1 := io.din
    }
    when(io.s2 === io.sd) {
      io.rs2 := io.din
    }
  }

/* Formal */
  assert(regs(0) === 0.U)

  when(io.s1 === 0.U) {
    assert(io.rs1 === 0.U)
  }.otherwise {
    when(io.s1 === io.sd && io.wen) {
      assert(io.rs1 === io.din)
    }
  }

  when(io.s2 === 0.U) {
    assert(io.rs2 === 0.U)
  }.otherwise {
    when(io.s2 === io.sd && io.wen) {
      assert(io.rs2 === io.din)
    }
  }

  when(past(io.sd =/= 0.U && io.wen)) {
    assert(regs(past(io.sd)) === past(io.din))
  }
}
