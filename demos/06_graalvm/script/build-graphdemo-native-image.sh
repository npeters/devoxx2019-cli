#!/usr/bin/env bash
cd /Users/npeters/workspaces/npeters/devoxx-cli/demos/06_graalvm/build/libs 
native-image \
-H:+PrintAnalysisCallTree \
--report-unsupported-elements-at-runtime \
-H:+ReportExceptionStackTraces \
-DGraphConst=true \
-cp ./build/libs/GraphDemo-0.0.1-all.jar \
-H:Name=graphdemo-graalvm \
GraphDemo