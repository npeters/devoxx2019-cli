#!/usr/bin/env bash

[[ -s ~/.sdkman/bin/sdkman-init.sh ]] && source ~/.sdkman/bin/sdkman-init.sh
sdk offline enable > /dev/null
sdk use java 11.0.2.hs-adpt

ZDOTDIR=$(pwd)  SHELL="zsh -d" ./demoit  demos
