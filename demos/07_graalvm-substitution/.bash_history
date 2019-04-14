./helloworldruntimemode-graalvm
bat --style numbers src/main/resources/META-INF/native-image/substitution/native-image.properties;
 native-image -jar build/libs/HelloWorldRuntimeMode-0.0.1-all.jar
java -jar build/libs/HelloWorldRuntimeMode-0.0.1-all.jar
