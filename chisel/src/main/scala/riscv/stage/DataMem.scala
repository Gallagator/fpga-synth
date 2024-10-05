package riscv.stage.datamem

import chisel3._
import chisel3.util._

/* This module is incredibly simple for now but may want 
 * to implement caching later */

class DataMemIo(width: Int) extends Bundle {
  val din = Input(UInt(width.W))
  val addr = Input(UInt(width.W))
  val wen = Input(Bool()) /* Write enable */
  val ren = Input(Bool()) /* Read enable */

  val dinOut = Output(UInt(width.W))
  val addrOut = Output(UInt(width.W))
  val wenOut = Output(Bool()) /* Write enable */
  val renOut = Output(Bool()) /* Read enable */
}

class DataMemStageControl extends Bundle {
  val wen = Input(Bool()) /* Write enable */
  val ren = Input(Bool()) /* Read enable */
}

class DataMemStageIn(width: Int) extends Bundle {
  val din = Input(UInt(width.W))
  val addr = Input(UInt(width.W))
  val control = Input(new DataMemStageControl)
}

class DataMemStageOut(width: Int) extends Bundle {
  val dinOut = Output(UInt(width.W))
  val addrOut = Output(UInt(width.W))
  val wenOut = Output(Bool()) /* Write enable */
  val renOut = Output(Bool()) /* Read enable */
}

class DataMemStageIO(width: Int) extends Bundle {
  val in = Input(new DataMemStageIn(width))
  val out = Output(new DataMemStageOut(width))
}

//class DataMemStage(width: Int) extends Bundle {
//  val io = IO(new DataMemStageIO(width))
//  val mem = Module(new DataMem())
//
//  io.out.dinOut := mem.io.dinOut
//  io.out.addrOut := mem.io.addrOut
//  io.out. := mem.io.
//  io.out. := mem.io.
//}

class DataMem(width: Int) extends Module {
  val io = IO(new DataMemIo(width))

  io.dinOut := io.din
  io.addrOut := io.addr
  io.wenOut := io.wen
  io.renOut := io.ren
}
