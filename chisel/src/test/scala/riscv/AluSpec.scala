package riscv.alu

import chisel3._
import chiseltest._
import chiseltest.formal._
import org.scalatest.flatspec.AnyFlatSpec
import chisel3.util._


class AluSpec extends AnyFlatSpec with ChiselScalatestTester with Formal {
  "Alu" should "pass bmc" in {
    verify(new Alu(32), Seq(BoundedCheck(1)))
  }
}
