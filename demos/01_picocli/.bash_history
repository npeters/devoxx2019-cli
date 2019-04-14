cat  ./src/main/bash/exec-template.sh ./build/libs/jwt-cli-0.0.1-all.jar > jwt-cli && chmod +x jwt-cli
bat --style numbers ./src/main/bash/exec-template.sh
java -jar ./build/libs/jwt-cli-0.0.1-all.jar -d -o json -t eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiYWRtaW4iLCJpc3MiOiJjYW5hbCIsIm5hbWUiOiJucGV0ZXJzIiwiZXhwIjoxNTU5MzQwMDAwfQ.pmx1_EJeULIM4OAzcBWqNAPPKM6PorIhwM-4ZSw20og | jq .
java -jar ./build/libs/jwt-cli-0.0.1-all.jar -i canal -c role=admin -c name=npeters
java -jar ./build/libs/jwt-cli-0.0.1-all.jar
