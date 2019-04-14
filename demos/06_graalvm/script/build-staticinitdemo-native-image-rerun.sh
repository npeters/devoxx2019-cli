#!/usr/bin/env bash
cd /Users/npeters/workspaces/npeters/devoxx-cli/demos/06_graalvm/build/libs
${JAVA_HOME}/bin/native-image \
--rerun-class-initialization-at-runtime="StaticInitDemo" \
-H:+PrintAnalysisCallTree \
--report-unsupported-elements-at-runtime \
-H:+ReportExceptionStackTraces \
-cp ./build/libs/StaticInitDemo-0.0.1-all.jar \
-H:Name=staticinitdemo-graalvm-rerun \
-Dproperty=propertyValue \
StaticInitDemo