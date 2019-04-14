#!/usr/bin/env bash

${JAVA_HOME}/bin/native-image \
-H:Path=./build/libs \
-H:+PrintAnalysisCallTree \
--report-unsupported-elements-at-runtime \
-H:+ReportExceptionStackTraces \
-cp ./build/libs/StaticInitDemo-0.0.1-all.jar \
-H:Name=staticinitdemo-graalvm \
-Dproperty=propertyGiveAtCompileTime \
StaticInitDemo
