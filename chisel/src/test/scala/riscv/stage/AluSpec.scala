package riscv.stage.alu

import chisel3._
import chiseltest._
import chiseltest.formal._
import org.scalatest.flatspec.AnyFlatSpec
import chisel3.util._


class AluSpec extends AnyFlatSpec with ChiselScalatestTester with Formal {
  /* As it stands, Alu is fully combinational - we need only a bounded check
   * of 1 cycle */
  "Alu" should "pass bmc for an ALU of size 32" in {
    verify(new Alu(32), Seq(BoundedCheck(1)))
  }
  "Alu" should "pass bmc for an ALU of size 64" in {
    verify(new Alu(64), Seq(BoundedCheck(1)))
  }

}
