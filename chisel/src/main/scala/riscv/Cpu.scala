package riscv.cpu

import riscv.controller
import riscv.stage._
import chisel3._
import chisel3.util._
import chisel3.util.experimental._

// abstract class PipelineReg[T <: Data] {
//   def stall(): Unit
//   def value(): T
// }

class PipelineRegIO[T <: Data](gen: T) extends Bundle {
  val in = Input(gen)
  val stall = Input(Bool())
  val out = Output(gen)
}

class PipelineRegPassthrough[T <: Data](init: T) extends Module {
  val io = IO(new PipelineRegIO(init.cloneType.asInstanceOf[T]))

  private val reg = RegInit(init)
  private val stalled = RegInit(false.B)

  stalled := io.stall

  when(!io.stall) {
    reg := io.in
  }

  io.out := reg
}

class PipelineRegComb[T <: Data](init: T) extends Module {
  val io = IO(new PipelineRegIO(init.cloneType.asInstanceOf[T]))

  private val reg = RegInit(init)

   when(!io.stall) {
    reg := io.in
  }
  io.out := reg
}

class FetchMemRequest(xlen: Int, instrWidth: Int) extends Bundle {
  val addr = Decoupled(UInt(xlen.W))
  val instr = Flipped(Decoupled(UInt(instrWidth.W)))
}

class FetchStage(xlen: Int, instrWidth: Int) extends Module {
  val io = IO(new Bundle {
    val instr = Decoupled(UInt(xlen.W))
    val memRequest = new FetchMemRequest(xlen, instrWidth)
  })

  val fetcher = Module(new fetch.Fetch(xlen));
  fetcher.io.stall := !io.instr.ready || !io.memRequest.addr.ready
  /* These will be used when branching is implemented */
  fetcher.io.pcWen := false.B
  fetcher.io.pcIn := 0.U
  
  io.memRequest.addr.valid := fetcher.io.ren;
  io.memRequest.addr.bits := fetcher.io.pcOut;

  /* We are ready to make a memory request when the sink is ready*/
  io.memRequest.instr.ready := io.instr.ready

  val instrReg = Module(new PipelineRegPassthrough(0.U(xlen.W)))
  instrReg.io.in := io.memRequest.instr.bits
  instrReg.io.stall := !io.instr.ready
  io.instr.valid := io.memRequest.instr.valid && io.instr.ready
  /* Push through NOP - TODO: Should really make a NOP object rather then 0.U??? */
  io.instr.bits := Mux(io.instr.valid, instrReg.io.out, 0.U)
}

class Cpu(xlen: Int, instrWidth: Int, numregs: Int, programFile: String)
  extends Module {
  require(programFile.nonEmpty)


  /* Initialise SRAM with 1 read-port for fetching and 1 read-write port for
   * load/store. Initialise the memory with the binary file with location:
   * programFile */
  val mem = SRAM(1024, UInt(xlen.W), 1, 0, 1, BinaryMemoryFile(programFile))

  /* ---- Fetch stage ---- */

  val fetchStage = Module(new FetchStage(xlen, instrWidth))
  /* Memory is always ready to be queried ONLY WITH SRAM WHEN CACHING THIS WILL
   * CHANGE. 
   * Wire up memRequest */
  fetchStage.io.memRequest.addr.ready := true.B 
  mem.readPorts(0).enable := fetchStage.io.memRequest.addr.valid;
  mem.readPorts(0).address := fetchStage.io.memRequest.addr.bits;

  /* Memory request is valid if a read was issued last cycle. This is only the
   * case because we have a single cycle SRAM - this is subject to change */
  val readReqValid = RegInit(false.B)
  readReqValid := mem.readPorts(0).enable 
  fetchStage.io.memRequest.instr.bits := mem.readPorts(0).data
  fetchStage.io.memRequest.instr.valid := readReqValid

  fetchStage.io.instr.ready := true.B /* Always ready as we dont have any stalls yet. */

  val instr = Mux(fetchStage.io.instr.fire, fetchStage.io.instr.bits, 0.U)

  /* ---- Reg/Decode Stage ---- */
  val cont = Module(new controller.Controller(numregs, instrWidth))
  cont.io.in := instr

  /* ---- ALU Stage ---- */

  /* ---- Mem Stage ---- */
  /* TODO: connect up! */
  mem.readwritePorts(0).isWrite := false.B
  mem.readwritePorts(0).enable := false.B
  mem.readwritePorts(0).writeData := 0.U
  mem.readwritePorts(0).address := 0.U

  /* ---- Write-back Stage ---- */

}
