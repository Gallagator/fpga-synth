package riscv.fetch

import chisel3._
import chisel3.util._
import chiseltest.formal._

class FetchControl(width: Int) extends Bundle {
  val stall = Input(Bool())
  val pcWen = Input(Bool())
  val pc = Input(UInt(width.W))
}

class FetchOut(width: Int) extends Bundle {
  val pc = Output(UInt(width.W))
  val readEn = Output(Bool())
}

class Fetch(width: Int, pcInit: BigInt = 0) extends Module {
  val in = IO(new FetchControl(width))
  val out = IO(new FetchOut(width))

  val pc = RegInit(pcInit.U(width.W))
  out.pc := pc
  out.readEn := !in.stall

  when(!in.stall) {
    when(in.pcWen) {
      pc := in.pc
    }.otherwise {
      pc := pc + (width / 8).U
    }
  }

/* Formal */
  when(past(in.stall)) {
    stable(pc)
  }
  
}
