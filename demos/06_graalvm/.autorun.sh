#!/usr/bin/env bash

[[ -s ~/.sdkman/bin/sdkman-init.sh ]] && source ~/.sdkman/bin/sdkman-init.sh
sdk offline enable > /dev/null
sdk use java 1.0.0-rc-15-grl