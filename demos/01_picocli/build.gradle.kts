import groovy.xml.dom.DOMCategory.attributes
import org.gradle.api.tasks.compile.JavaCompile
import org.apache.tools.ant.filters.*
import org.gradle.internal.impldep.com.google.common.io.Files

plugins {
    `java-library`
    java
    id("com.github.johnrengelman.shadow") version "5.0.0"
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
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}


dependencies {
    implementation("info.picocli", "picocli", "3.9.5")
    implementation("com.auth0", "java-jwt", "3.8.0")
    implementation("com.fasterxml.jackson.core", "jackson-databind", "2.9.8")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}


tasks.jar {
    manifest {
        attributes(
                "Implementation-Version" to project.version,
                "Implementation-Vendor" to "Canal+",
                "Implementation-Title" to "jwt-cli",
                "Main-Class" to "JwtCli"
        )
    }
}

tasks.register("copy-dependencies") {
    copy {
        into("lib")
        from(configurations.default)
    }
}

tasks.register("exec") {
    dependsOn("shadowJar")
    
    doLast {
        val execBytes = File("${project.projectDir}/src/main/bash/exec-template.sh").readBytes()
        val jarBytes = File("${project.buildDir}/libs/${project.name}-$version-all.jar").readBytes()
        val exe = File("${project.buildDir}/libs/jwt-cli")
        exe.writeBytes(execBytes + jarBytes)
        exe.setExecutable(true)
    }
}
