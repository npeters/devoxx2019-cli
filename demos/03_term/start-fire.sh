#!/usr/bin/env bash 
source "/Users/npeters/.sdkman/bin/sdkman-init.sh"
sdk offline enable
sdk u java 11.0.2.hs-adpt
reset
./term-demo fire
