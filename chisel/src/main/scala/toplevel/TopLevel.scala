package top

import chisel3._
import blinky.Blinky
import circt.stage.ChiselStage

class TopLevel extends Module {
  val led0 = IO(Output(Bool()))

  val blinky = Module(new Blinky(125000000, 1))

  led0 := blinky.out
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
