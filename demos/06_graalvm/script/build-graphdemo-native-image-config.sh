#!/usr/bin/env bash
cd /Users/npeters/workspaces/npeters/devoxx-cli/demos/06_graalvm/build/libs 
/Users/npeters/.sdkman/candidates/java/1.0.0-rc-15-grl/bin/native-image \
-H:+PrintAnalysisCallTree \
-H:+ReportExceptionStackTraces \
-DGraphConst=true \
--report-unsupported-elements-at-runtime \
--rerun-class-initialization-at-runtime="GraphDemo\$GraphConst" \
-H:ConfigurationFileDirectories=/Users/npeters/workspaces/npeters/devoxx-cli/demos/06_graalvm/src/main/config/graph \
-cp ./build/libs/GraphDemo-0.0.1-all.jar \
-H:Name=graphdemo-graalvm-config \
GraphDemo