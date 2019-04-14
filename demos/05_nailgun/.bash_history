time ./ng JwtCli encode -i npe -c group=admin
java -cp 'lib/*:../02_picocli-v2/build/libs/jwt-cli-v2-0.0.2-all.jar:build/libs/demo-nailgun-0.0.1.jar' com.facebook.nailgun.NGServer
