#!/usr/bin/env bash
native-image \
-H:Path=/Users/npeters/workspaces/npeters/devoxx-cli/demos/06_graalvm/build/libs \
-H:+LogVerbose  \
-H:Dump=CodeGen,CodeInstall -H:+PrintCFG  -H:+ShowDumpFiles -H:+DumpOnError \
-H:PrintGraph=File -H:+PrintGraphFile \
-H:+PrintAnalysisCallTree \
-H:+ReportExceptionStackTraces \
-H:ConfigurationFileDirectories=/Users/npeters/workspaces/npeters/devoxx-cli/demos/06_graalvm/src/main/config/graph \
-cp ./build/libs/GraphDemo-0.0.1-all.jar \
-H:Name=graphdemo-graalvm-config \
GraphDemo