package riscv.cpu

import chisel3._
import chisel3.simulator.EphemeralSimulator._
import org.scalatest.flatspec.AnyFlatSpec

class FetchStageSpec extends AnyFlatSpec {
  behavior of "FetchStage"
  it should "work" in {
    simulate(new FetchStage(32, 32)) { c =>
      /* Rest of pipeling not ready for instr */
      c.io.instr.ready.poke(false.B)
      /* Memory unit not ready for a memRequest */
      c.io.memRequest.addr.ready.poke(false.B)
      c.io.memRequest.instr.bits.poke(32.U)
      c.io.memRequest.instr.valid.poke(false.B)
      c.io.memRequest.addr.valid.expect(false.B)
      c.io.instr.valid.expect(false.B)
      c.clock.step()

      c.io.memRequest.addr.ready.poke(true.B)
      c.io.memRequest.addr.bits.expect(0.U)
      c.io.memRequest.addr.valid.expect(true.B)
      c.io.instr.valid.expect(false.B)
    }
  }
}
