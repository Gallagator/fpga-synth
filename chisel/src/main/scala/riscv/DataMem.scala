package riscv.datamem

import chisel3._
import chisel3.util._

/* This module is incredibly simple for now but may want 
 * to implement caching later */

class DataMemControl(width: Int) extends Bundle {
  val din = Input(UInt(width.W))
  val addr = Input(UInt(width.W))
  val wen = Input(Bool()) /* Write enable */
  val ren = Input(Bool()) /* Read enable */
}

class DataMemOut(width: Int) extends Bundle {
  val din = Output(UInt(width.W))
  val addr = Output(UInt(width.W))
  val wen = Output(Bool()) /* Write enable */
  val ren = Output(Bool()) /* Read enable */
}

class DataMem extends Module {

}
