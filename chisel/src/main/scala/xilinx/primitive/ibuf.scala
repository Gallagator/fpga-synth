package xilinx.primitive

import chisel3._

class IBUF[T <: Data](gen: T) extends BlackBox {
  val io = IO(new Bundle {
    val I = Input(gen)
    val O = Output(gen)
  })
}
