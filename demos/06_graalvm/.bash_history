ls src/main/config/graph-agent/
./native-image-configure process-trace --output-dir=src/main/config/graph-agent/ src/main/config/graph-agent/trace-file.json
java -agentpath:$(pwd)/native-image-agent.dylib=trace-output=$(pwd)/src/main/config/graph-agent/trace-file.json -cp 'build/libs/GraphDemo-0.0.1-all.jar' GraphDemo
./graphdemo-graalvm-config
bat src/main/config/graph/*.json
./graphdemo-graalvm
./staticinitdemo-graalvm-rerun  -Dproperty=Devoxx
./build/libs/staticinitdemo-graalvm  -Dproperty=Devoxx
bat script/build-staticinitdemo-native-image-property.sh; script/build-staticinitdemo-native-image-property.sh
