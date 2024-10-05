package top

import chisel3._
import blinky.Blinky
import circt.stage.ChiselStage
import pll.Pll

class TopLevel extends Module {
  val led0 = IO(Output(Bool()))

  /* Instantiate PLL for input 125MHz and output 50 MHz
   * 50 = 125 / 5 / 17 * 34*/
  val pll = Module(new Pll(5, 34, 17, 8.0))
  pll.io.clk_in := clock
  pll.io.reset := false.B

  withClockAndReset(pll.io.clk_out, false.B) {

    val blinky = Module(new Blinky(50_000_000, 1))

    led0 := blinky.out
  }
}

import java.nio.file.{Paths, Files}
import java.nio.charset.StandardCharsets

object Main extends App {
  val folder_name = Paths.get(".", "generated")
  if (!Files.exists(folder_name)) {
    Files.createDirectory(folder_name)
  }

  def writeModule(gen: => RawModule, name: String) : Unit = {
    val verilog = ChiselStage.emitSystemVerilog(
      gen,
      firtoolOpts = Array("-disable-all-randomization", "-strip-debug-info")
    )

    Files.write(
      Paths.get(".", "generated", name),
      verilog.getBytes(StandardCharsets.UTF_8)
    )
  }

  writeModule(new TopLevel(), "toplevel.sv")
  // Circt can't generate the following as it doesn't support past signals yet...
  //writeModule(new regfile.Regfile(32, 32), "regfile.sv")
}
