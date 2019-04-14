plugins {
    `java-library`
    java
    id("com.github.johnrengelman.shadow")
}

repositories {
    mavenLocal()
    jcenter()
    mavenCentral(
            mapOf(Pair("url", "https://repo1.maven.org/maven2"))
    )
}

version = "0.0.1"



configure<JavaPluginConvention> {
    sourceCompatibility = org.gradle.api.JavaVersion.VERSION_1_8
    targetCompatibility = org.gradle.api.JavaVersion.VERSION_1_8
}

val graalvmVersion = "1.0.0-rc14"

dependencies {
    implementation("info.picocli", "picocli", "3.9.5")
    implementation("com.auth0", "java-jwt", "3.8.0")
    implementation("com.fasterxml.jackson.core", "jackson-databind", "2.9.8")
    implementation("com.pivovarit", "throwing-function", "1.5.0")
    implementation("com.pivovarit", "throwing-function", "1.5.0")
    implementation("com.pivovarit", "throwing-function", "1.5.0")
    implementation("org.graalvm.sdk", "graal-sdk", graalvmVersion)
    implementation("com.oracle.substratevm", "svm", graalvmVersion) {
        exclude("*", "*")
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}



tasks.register("copy-dependencies") {
    copy {
        into("lib")
        from(configurations.default)
    }
}


val mains = listOf("HelloWorldRuntimeMode")

mains.forEach { main ->


    tasks.register<Jar>("build-$main") {
        dependsOn("shadowJar")
        val outfile = "${project.buildDir}/libs/${main}-${project.version}-all.jar"
        outputs.file(outfile)
        archiveFileName.set("${main}-${project.version}-all.jar")
        from(zipTree("${project.buildDir}/libs/${project.name}-${project.version}-all.jar"))
        manifest {
            attributes(
                    "Implementation-Version" to project.version,
                    "Main-Class" to main
            )
        }

    }


    tasks.register("exec-$main") {
        dependsOn("build-$main")
        val outfile = "${project.buildDir}/libs/${main.toLowerCase()}-hotspot"
        outputs.file(outfile)
        doLast {
            val execBytes = file("${project.projectDir}/src/main/bash/exec-template.sh").readBytes()
            val jarBytes = file("${project.buildDir}/libs/$main-${project.version}-all.jar").readBytes()
            val exe = file(outfile)

            exe.writeBytes(execBytes + jarBytes)
            exe.setExecutable(true)
        }
    }


    tasks.register("native-script-$main") {

        doLast {

            val build = File("script/build-${main.toLowerCase()}-native-image.sh")

            val cmd = listOf(
                    "\${JAVA_HOME}/bin/native-image",
                    "-H:+PrintAnalysisCallTree",
                    "-H:+ReportExceptionStackTraces",
                    "--report-unsupported-elements-at-runtime",
                    "-cp ${project.buildDir}/libs/$main-${project.version}-all.jar",
                    "-H:Name=${main.toLowerCase()}-graalvm",
                    main
            )

            val script = "#!/usr/bin/env bash\n" +
                    "cd ${project.buildDir}/libs \n" +
                    cmd.joinToString(" \\\n")

            build.writeBytes(script.toByteArray(Charsets.UTF_8))
            build.setExecutable(true)
        }


    }

    tasks.register<Exec>("build-native-$main") {
        dependsOn("build-$main")
        commandLine = listOf("script/build-${main.toLowerCase()}-native-image.sh")

    }

}

tasks.register("all") {
    mains.forEach { main ->
        run {
            dependsOn("build-native-$main")
        }
    }
}

