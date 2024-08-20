package riscv.regfile

import chisel3._
import chiseltest._
import chiseltest.formal._
import org.scalatest.flatspec.AnyFlatSpec
import chisel3.util._


class RegfileSpec extends AnyFlatSpec with ChiselScalatestTester with Formal {
  "Regfile" should "pass bmc" in {
    verify(new Regfile(32, 32), Seq(BoundedCheck(1)))
  }
}
