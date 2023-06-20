@file:Suppress("UnstableApiUsage")

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(libs.java.inject)
    implementation(libs.kotlin.coroutine.core)
}
