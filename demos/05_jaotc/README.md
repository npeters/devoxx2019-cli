## JEP 295: Ahead-of-Time Compilation JAOTC


_http://openjdk.java.net/jeps/295_

Compile Java classes to native code prior to launching the virtual machine. 

`javac HelloWorld.java`

`jaotc --output libHelloWorld.so HelloWorld.class`

`jaotc --output libjava.base.so --module java.base`

`java -XX:AOTLibrary="./libjava.base.so,./libHelloWorld.so" HelloWorld`

