package top

import chisel3._
import blinky.Blinky
import circt.stage.ChiselStage
import pll.Pll

class TopLevel extends Module {
  val led0 = IO(Output(Bool()))

  val pll = Module(new Pll)
  pll.io.clk_in := clock
  pll.io.reset := reset

  withClockAndReset(pll.io.clk_out, reset) {

    val blinky = Module(new Blinky(50_000_000, 1))

    led0 := blinky.out
  }
}

import java.nio.file.{Paths, Files}
import java.nio.charset.StandardCharsets

object Main extends App {
  val verilog = ChiselStage.emitSystemVerilog(
    new TopLevel(),
    firtoolOpts = Array("-disable-all-randomization", "-strip-debug-info")
  )

  val folder_name = Paths.get(".", "generated")
  if (!Files.exists(folder_name)) {
    Files.createDirectory(folder_name)
  }

  Files.write(
    Paths.get(".", "generated", "toplevel.sv"),
    verilog.getBytes(StandardCharsets.UTF_8)
  )
}
