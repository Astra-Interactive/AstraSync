plugins {
    java
    `maven-publish`
    `java-library`
    kotlin("jvm") version "1.7.0"
    kotlin("plugin.serialization") version "1.7.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.astrainteractive"
version = "2.4.0"
java {
    withSourcesJar()
    withJavadocJar()
    java.sourceCompatibility = JavaVersion.VERSION_1_8
    java.targetCompatibility = JavaVersion.VERSION_17
}
repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://nexus.scarsz.me/content/groups/public/")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://repo.essentialsx.net/snapshots/")
    maven("https://repo.maven.apache.org/maven2/")
    maven("https://repo.maven.apache.org/maven2/")
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo1.maven.org/maven2/")
    maven("https://m2.dv8tion.net/releases")
    maven("https://maven.playpro.com")
    maven {
        url = uri("https://maven.pkg.github.com/Astra-Interactive/AstraLibs")
        val config = project.getConfig()
        credentials {
            username = config.username
            password = config.password
        }
    }
    maven("https://jitpack.io")
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        url = uri("https://mvn.lumine.io/repository/maven-public/")
        metadataSources {
            artifact()
        }
    }
    flatDir { dirs("libs") }
}

dependencies {
    implementation("ru.astrainteractive.astralibs:ktx-core:${Dependencies.Kotlin.astraLibs}")
    implementation(Dependencies.Implementation.jdbc)
    implementation(Dependencies.Implementation.exposedJavaTime)
    implementation(Dependencies.Implementation.exposedJDBC)
    implementation(Dependencies.Implementation.exposedCORD)
    implementation(Dependencies.Implementation.exposedDAO)
    // Serialization
    implementation(Dependencies.Implementation.kotlinxSerialization)
    implementation(Dependencies.Implementation.kotlinxSerializationJson)
    implementation(Dependencies.Implementation.kotlinxSerializationYaml)
    // AstraLibs
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    testImplementation(kotlin("test"))
    testImplementation("org.testng:testng:7.1.0")

}

tasks {
    withType<JavaCompile>() {
        options.encoding = "UTF-8"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
}
tasks.test {
    useJUnitPlatform()
}
