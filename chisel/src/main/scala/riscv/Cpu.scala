package riscv.cpu

import riscv.controller
import riscv.stage._
import chisel3._
import chisel3.util._
import chisel3.util.experimental._

abstract class PipelineReg[T <: Data] {
  def stall(): Unit
  def value(): T
}

class PipelineRegPassthrough[T <: Data](in: T, init: T) extends PipelineReg[T] {
  private val reg = RegInit(init)
  private val stalled = RegInit(false.B)
  private val isStall = false.B

  stalled := isStall
  def stall() = {
    isStall := true.B
  }

  when(!isStall) {
    reg := in
  }

  val value = WireInit(Mux(stalled, reg, in))
}

class PipelineRegComb[T <: Data](in: T, init: T) extends PipelineReg[T] {
  private val isStall = false.B
  private val reg = RegInit(init)

  def stall() = {
    isStall := true.B
  }

   when(!isStall) {
    reg := in
  }
  val value = WireInit(reg)
}

class Cpu(xlen: Int, instrWidth: Int, numregs: Int, programFile: String)
  extends Module {
  require(programFile.nonEmpty)

  val mem = SRAM(1024, UInt(xlen.W), 1, 0, 1, BinaryMemoryFile(programFile))

  /* ---- Fetch stage ---- */
  val fetcher = Module(new fetch.Fetch(xlen));

  /* These will be used when branching and stalling is implemented */
  fetcher.io.stall := false.B
  fetcher.io.pcWen := false.B
  fetcher.io.pcIn := 0.U

  /* Initialise SRAM with 1 read-port for fetching and 1 read-write port for
   * load/store. Initialise the memory with the binary file with location:
   * programFile */
  mem.readPorts(0).enable := fetcher.io.ren;
  mem.readPorts(0).address := fetcher.io.pcOut
  val instr = new PipelineRegPassthrough(mem.readPorts(0).data, 0.U)

  /* ---- Reg/Decode Stage ---- */
  val cont = Module(new controller.Controller(numregs, instrWidth))
  cont.io.in := instr.value

  /* TODO: connect up! */
  mem.readwritePorts(0).isWrite := false.B
  mem.readwritePorts(0).enable := false.B
  mem.readwritePorts(0).writeData := 0.U
  mem.readwritePorts(0).address := 0.U

}
