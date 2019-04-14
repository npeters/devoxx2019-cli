rootProject.name = "jwt-cli-v2"

pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.github.johnrengelman.shadow") {
                useVersion("5.0.0")
            }
        }
    }
}