rootProject.name = "jline-demo"
include("jwtcli")
project(":jwtcli").projectDir = file("../02_picocli-v2")

pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.github.johnrengelman.shadow") {
                useVersion("5.0.0")
            }
        }
    }
}