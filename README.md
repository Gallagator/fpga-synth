# fpga synth

The aim of this project is to generate some samples on an fpga and output them
through an I2S DAC - No idea how far this'll go but likely not very

## Dependencies

- java
- sbt
- fusesoc
- vivado
- probably something else!

As much HDL as possible is written in [Chisel](https://github.com/chipsalliance/chisel)
and this is compiled down to system verilog with sbt. sbt is invoked through the
[fusesoc](https://github.com/olofk/fusesoc) build system.

## Supported Boards:

- pynq-z2

Supporting further boards is rather trivial. Simply add your board to the
synth.core file and give it a fileset dependency for its constraints file
(xdc for vivado, pdc for quartus - eww)

## Building:
 
This project depends on [fusesoc-generators](https://github.com/fusesoc/fusesoc-generators)
And so upon cloning the library needs to be added with the following

``` zsh
fusesoc library add fusesoc_utils_generators https://github.com/fusesoc/fusesoc-
generators
```

To target the pynq-z2 board run:
``` zsh
fusesoc run --target=pynq-z2 --tool=vivado ligallag:proj:synth:0.0.1
```
This will generate a .bit file that can be flashed to the through your vendor.
tool.

Right now, this will only blink an LED on the FPGA - who'd have thought it'd be so hard!
