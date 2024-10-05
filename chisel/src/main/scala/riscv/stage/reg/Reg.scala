package riscv.stage.reg

import riscv.stage.regfile.Regfile
import chisel3._
import chisel3.util._

/* Control inputs */
class RegStageControl(numregs: Int, width: Int) extends Bundle {
  val selectWidth = log2Ceil(numregs).W
  val s1 = Input(UInt(selectWidth))
  val s2 = Input(UInt(selectWidth))
  val sd = Input(UInt(selectWidth))
}

class WbStageControl extends Bundle {
  val wen = Input(Bool())
}

/* IO */
class RegStageIn(numregs: Int, width: Int) extends Bundle {
  val controlReg = Input(new RegStageControl(numregs, width))
  val controlWb = Input(new WbStageControl)
  val din = Input(UInt(width.W));
}

class RegStageOut(width: Int) extends Bundle {
  val rs1 = Output(UInt(width.W))
  val rs2 = Output(UInt(width.W))
}

class RegStageIO(numregs: Int, width: Int) extends Bundle {
  val in = Input(new RegStageIn(numregs, width))
  val out = Output(new RegStageOut(width))
}

class RegStage(numregs: Int, width: Int) extends Module { 
  val io = IO(new RegStageIO(numregs, width))

  val regfile = Module(new Regfile(numregs, width))

  regfile.io.s1 := io.in.controlReg.s1
  regfile.io.s2 := io.in.controlReg.s2
  regfile.io.sd := io.in.controlReg.sd
  regfile.io.din := io.in.din
  regfile.io.wen := io.in.controlWb.wen

  io.out.rs1 := regfile.io.rs1
  io.out.rs2 := regfile.io.rs2
}

