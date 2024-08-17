package riscv.regfile

import chisel3._
import chisel3.util._

class Regfile(numregs: Int, width: Int) extends Module {
  val selectWidth = log2Ceil(numregs).W
  val io = IO(new Bundle {
    val s1 = Input(UInt(selectWidth))
    val s2 = Input(UInt(selectWidth))
    val sd = Input(UInt(selectWidth))
    val din = Input(UInt(width.W))
    val wen = Input(Bool())
    val rs1 = Output(UInt(width.W))
    val rs2 = Output(UInt(width.W))
  })

  val regs = RegInit(VecInit(Seq.fill(numregs)(0.U(width.W))))

  io.rs1 := regs(io.s1)
  io.rs2 := regs(io.s2)

  when(io.sd =/= 0.U && io.wen) {
    regs(io.sd) := io.din
  }
}
