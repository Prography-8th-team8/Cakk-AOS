@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.konan.properties.Properties

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.serialization)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.dagger.hilt)
}

android {
    namespace = "org.prography.cakk.data"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
        buildConfigField("String", "CAKK_BASE_URL", properties.getProperty("CAKK_BASE_URL"))
    }
}
dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:utility"))

    implementation(libs.gson)
    implementation(libs.bundles.dagger)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.bundles.ktor)
    implementation(libs.material)
    implementation(libs.junit)
    implementation(libs.androidx.test.junit)
    implementation(libs.androidx.test.espresso)
    kapt(libs.bundles.compiler)
}


