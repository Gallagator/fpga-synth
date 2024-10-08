package pll;

import chisel3._
import xilinx.primitive.{IBUF, BUFG}
import xilinx.primitive.series7000.PLLE2_ADV

/*
 * @divclk - divider for input clock
 * @clkfboutMult - clock multiplier
 * @clkout0Div - clock output 0 divider
 * @clkin1Period - period in ns of input clock
 * */
class Pll(
    divclk: Int,
    clkfboutMult: Int,
    clkout0Div: Int,
    clkin1Period: Double
) extends Module {
  val io = IO(new Bundle {
    val clk_in = Input(Clock())
    val reset = Input(Bool())
    val clk_out = Output(Clock())
    val locked = Output(Bool())
  })

  val ibuf = Module(new IBUF(Clock()))
  ibuf.io.I := io.clk_in
  val clk_in_buf = ibuf.io.O

  val plle2_adv = Module(
    new PLLE2_ADV(
      divclk_divide = divclk,
      clkfbout_mult = clkfboutMult,
      clkfbout_phase = 0.0,
      clkout0_divide = clkout0Div,
      clkout0_phase = 0.0,
      clkout0_duty_cycle = 0.5,
      clkin1_period = clkin1Period
    )
  )

  val feedbackBufg = Module(new BUFG)

  val clkfbout = plle2_adv.io.CLKFBOUT
  val clkOut = plle2_adv.io.CLKOUT0

  feedbackBufg.io.I := clkfbout

  plle2_adv.io.CLKFBIN := feedbackBufg.io.O
  plle2_adv.io.CLKIN1 := clk_in_buf
  plle2_adv.io.CLKIN2 := false.B.asClock
  plle2_adv.io.CLKINSEL := true.B
  plle2_adv.io.DADDR := 0.U
  plle2_adv.io.DCLK := false.B.asClock
  plle2_adv.io.DEN := false.B
  plle2_adv.io.DI := 0.U
  plle2_adv.io.DWE := false.B
  plle2_adv.io.PWRDWN := false.B
  plle2_adv.io.RST := reset

  io.locked := plle2_adv.io.LOCKED

  val clkoutBuf = Module(new BUFG)
  clkoutBuf.io.I := clkOut

  io.clk_out := clkoutBuf.io.O
}
