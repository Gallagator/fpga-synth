package xilinx.primitive

import chisel3._

class BUFG extends BlackBox {
  val io = IO(new Bundle {
    val I = Input(Clock())
    val O = Output(Clock())
  })
}
