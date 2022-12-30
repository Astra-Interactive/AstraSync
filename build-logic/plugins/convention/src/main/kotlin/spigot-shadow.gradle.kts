plugins {
    java
    `java-library`
    id("org.jetbrains.kotlin.jvm")
    id("com.github.johnrengelman.shadow")
}

tasks.shadowJar {
    dependencies {
        include(dependency(libs.kotlinGradlePlugin.get()))
        include(dependency(libs.coroutines.core.get()))
        include(dependency(libs.coroutines.coreJvm.get()))
        include(dependency(libs.kotlin.serialization.get()))
        include(dependency(libs.kotlin.serializationJson.get()))
        include(dependency(libs.kotlin.serializationKaml.get()))
        include(dependency(libs.exposed.core.get()))
        include(dependency(libs.exposed.dao.get()))
        include(dependency(libs.exposed.jdbc.get()))
        include(dependency(libs.mysql.connector.java.get()))
    }
    isReproducibleFileOrder = true
    mergeServiceFiles()
    dependsOn(configurations)
    archiveClassifier.set(null as String?)
    from(sourceSets.main.get().output)
    from(project.configurations.runtimeClasspath)
    minimize {
        exclude(dependency(libs.exposed.core.get()))
        exclude(dependency(libs.exposed.dao.get()))
        exclude(dependency(libs.exposed.jdbc.get()))
        exclude(dependency(libs.mysql.connector.java.get()))
    }
    archiveBaseName.set(libs.versions.name.get())
    destinationDirectory.set(File(libs.versions.destinationDirectoryPath.get()))
    destinationDirectory.set(File("D:\\Minecraft Servers\\EmpireProjekt_remake\\smp\\plugins"))

}
