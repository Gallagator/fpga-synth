package riscv.fetch

import chisel3._
import chisel3.util._
import chiseltest.formal._

/* Only slightly more complicated than the DataMem stage - of course may 
 * implement caching at some point. */

class FetchIo(width: Int) extends Bundle {
  val stall = Input(Bool())
  val pcWen = Input(Bool())
  val pcIn = Input(UInt(width.W))
  val pcOut = Output(UInt(width.W))
  val ren = Output(Bool()) /* Read enable */
}

class Fetch(width: Int, pcInit: BigInt = 0) extends Module {
  val io = IO(new FetchIo(width))

  val pc = RegInit(pcInit.U(width.W))
  io.pcOut := pc
  io.ren := !io.stall

  when(!io.stall) {
    when(io.pcWen) {
      pc := io.pcIn
    }.otherwise {
      pc := pc + (width / 8).U
    }
  }

/* Formal */
  when(past(io.stall)) {
    stable(pc)
  }
  
}
