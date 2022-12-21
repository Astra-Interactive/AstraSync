plugins {
    id("spigot-resource-processor")
    id("spigot-shadow")
    id("basic-java")
}

dependencies {
    // Kotlin
    implementation(libs.kotlinGradlePlugin)
    // Coroutines
    implementation(libs.coroutines.coreJvm)
    implementation(libs.coroutines.core)
    // Serialization
    implementation(libs.kotlin.serialization)
    implementation(libs.kotlin.serializationJson)
    implementation(libs.kotlin.serializationKaml)
    // AstraLibs
    implementation(libs.astralibs.ktxCore)
    implementation(libs.astralibs.spigotCore)
    implementation(libs.bstats.bukkit)
    // SQL
    implementation(libs.xerialSqliteJdbcLib)
    implementation(libs.exposed.java.time)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    // Test
    testImplementation(kotlin("test"))
    testImplementation(libs.orgTesting)
    // Spigot dependencies
    compileOnly(libs.essentialsx)
    compileOnly(libs.paperApi)
    compileOnly(libs.spigotApi)
    compileOnly(libs.spigot)
    // Local
    implementation(project(":domain"))
}