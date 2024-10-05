package riscv.controller

import riscv.stage._
import chisel3._

/* TODO: Can we add Bubble as a method on these control inputs? */
class ControlMem extends Bundle {
  val memWb = Output(new datamem.DataMemStageControl)
  val rest = Output(new reg.WbStageControl)
}

class ControlAlu extends Bundle {
  val aluMem = Output(new alu.AluStageControl)
  val rest = Output(new ControlMem)
}

class ControlReg(numregs: Int) extends Bundle {
  val regAlu = Output(new reg.RegStageControl(numregs))
  val rest = Output(new ControlAlu);
}

class ControllerIO(numregs: Int, instrwidth: Int) extends Bundle {
  val in = Input(UInt(instrwidth.W));
  val out = Output(new ControlReg(numregs));
}

class Controller(numregs: Int, instrwidth: Int) extends Module {
  val io = IO(new ControllerIO(numregs, instrwidth));

  /* Decide IO once I can! */
  io.out := DontCare;
}
