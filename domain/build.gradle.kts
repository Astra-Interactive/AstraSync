plugins {
    id("basic-java")
}
dependencies {
    // Kotlin
    implementation(libs.kotlinGradlePlugin)
    // AstraLibs
    implementation(libs.astralibs.ktxCore)
    // SQL
    implementation(libs.xerialSqliteJdbcLib)
    implementation(libs.exposed.java.time)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    // Serialization
    implementation(libs.kotlin.serialization)
    implementation(libs.kotlin.serializationJson)
    implementation(libs.kotlin.serializationKaml)
    // Test
    testImplementation(kotlin("test"))
    testImplementation(libs.orgTesting)

}