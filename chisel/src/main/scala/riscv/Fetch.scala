package riscv.fetch

import chisel3._
import chisel3.util._

class FetchControl(width: Int) extends Bundle {
  val stall = Input(Bool())
  val pcWen = Input(Bool())
  val pc = Input(UInt(width.W))
}

class FetchOut(width: Int) extends Bundle {
  val pc = Output(UInt(width.W))
}

class Fetch(width: Int, pcInit: BigInt = 0) extends Module {
  val in = IO(new FetchControl(width))
  val out = IO(new FetchOut(width))

  val pc = RegInit(pcInit.U(width.W))
  out.pc := pc

  when(!in.stall) {
    when(in.pcWen) {
      pc := in.pc
    }.otherwise {
      pc := pc + (width / 8).U
    }
  }
  
}
