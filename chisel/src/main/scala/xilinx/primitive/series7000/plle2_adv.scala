package xilinx.primitive.series7000

import chisel3._

class PLLE2_ADV(
    clkfbout_mult: BigInt = 5,
    clkfbout_phase: Double = 0.0,
    clkin1_period: Double = 0.0,
    clkin2_period: Double = 0.0,
    clkout0_divide: BigInt = 1,
    clkout1_divide: BigInt = 1,
    clkout2_divide: BigInt = 1,
    clkout3_divide: BigInt = 1,
    clkout4_divide: BigInt = 1,
    clkout5_divide: BigInt = 1,
    clkout0_duty_cycle: Double = 0.5,
    clkout1_duty_cycle: Double = 0.5,
    clkout2_duty_cycle: Double = 0.5,
    clkout3_duty_cycle: Double = 0.5,
    clkout4_duty_cycle: Double = 0.5,
    clkout5_duty_cycle: Double = 0.5,
    clkout0_phase: Double = 0.0,
    clkout1_phase: Double = 0.0,
    clkout2_phase: Double = 0.0,
    clkout3_phase: Double = 0.0,
    clkout4_phase: Double = 0.0,
    clkout5_phase: Double = 0.0,
    divclk_divide: BigInt = 1,
    ref_jitter1: Double = 0.01,
    ref_jitter2: Double = 0.01
) extends BlackBox(
      Map(
        "BANDWIDTH" -> "OPTIMIZED", /* TODO should use an enum input */
        "CLKFBOUT_MULT" -> clkfbout_mult,
        "CLKFBOUT_PHASE" -> clkfbout_phase,
        "CLKIN1_PERIOD" -> clkin1_period,
        "CLKIN2_PERIOD" -> clkin2_period,
        "CLKOUT0_DIVIDE" -> clkout0_divide,
        "CLKOUT1_DIVIDE" -> clkout1_divide,
        "CLKOUT2_DIVIDE" -> clkout2_divide,
        "CLKOUT3_DIVIDE" -> clkout3_divide,
        "CLKOUT4_DIVIDE" -> clkout4_divide,
        "CLKOUT5_DIVIDE" -> clkout5_divide,
        "CLKOUT0_DUTY_CYCLE" -> clkout0_duty_cycle,
        "CLKOUT1_DUTY_CYCLE" -> clkout1_duty_cycle,
        "CLKOUT2_DUTY_CYCLE" -> clkout2_duty_cycle,
        "CLKOUT3_DUTY_CYCLE" -> clkout3_duty_cycle,
        "CLKOUT4_DUTY_CYCLE" -> clkout4_duty_cycle,
        "CLKOUT5_DUTY_CYCLE" -> clkout5_duty_cycle,
        "CLKOUT0_PHASE" -> clkout0_phase,
        "CLKOUT1_PHASE" -> clkout1_phase,
        "CLKOUT2_PHASE" -> clkout2_phase,
        "CLKOUT3_PHASE" -> clkout3_phase,
        "CLKOUT4_PHASE" -> clkout4_phase,
        "CLKOUT5_PHASE" -> clkout5_phase,
        "COMPENSATION" -> "ZHOLD",
        "DIVCLK_DIVIDE" -> divclk_divide,
        "REF_JITTER1" -> ref_jitter1,
        "REF_JITTER2" -> ref_jitter2,
        "STARTUP_WAIT" -> "FALSE"
      )
    ) {
  val io = IO(new Bundle {
    val CLKFBIN = Input(Clock())
    val CLKFBOUT = Output(Clock())
    val CLKINSEL = Input(Bool())
    val CLKIN1 = Input(Clock())
    val CLKIN2 = Input(Clock())
    val CLKOUT0 = Output(Clock())
    val CLKOUT1 = Output(Clock())
    val CLKOUT2 = Output(Clock())
    val CLKOUT3 = Output(Clock())
    val CLKOUT4 = Output(Clock())
    val CLKOUT5 = Output(Clock())
    val DADDR = Input(UInt(7.W))
    val DCLK = Input(Clock())
    val DEN = Input(Bool())
    val DI = Input(UInt(16.W))
    val DO = Output(UInt(16.W))
    val DRDY = Output(Bool())
    val DWE = Input(Bool())
    val LOCKED = Output(Bool())
    val PWRDWN = Input(Bool())
    val RST = Input(Bool())
  })
}
