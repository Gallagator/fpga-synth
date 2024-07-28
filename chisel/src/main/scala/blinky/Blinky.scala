// See README.md for license details.

package blinky

import chisel3._
import chisel3.util.Counter

class Blinky(clk_freq: Int, blink_freq: Int) extends Module {
  val out = IO(Output(Bool()))

  val state = RegInit(Bool(), false.B)

  out := state

  val (_, wrap) = Counter(true.B, clk_freq / blink_freq)
  when(wrap) {
    state := !state
  }
}
