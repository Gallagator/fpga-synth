package riscv.stage.fetch

import chisel3._
import chiseltest._
import chiseltest.formal._
import org.scalatest.flatspec.AnyFlatSpec
import chisel3.util._


class FetchSpec extends AnyFlatSpec with ChiselScalatestTester with Formal {
  "Fetch" should "pass bmc for width of" in {
    verify(new Fetch(32), Seq(BoundedCheck(1)))
  }
}
