@file:Suppress("UnstableApiUsage")

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.serialization)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.dagger.hilt)
}

android {
    namespace = "org.prography.network"

    defaultConfig {
        buildConfigField("String", "CAKK_BASE_URL", gradleLocalProperties(rootDir).getProperty("CAKK_BASE_URL"))
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.dagger.hilt.android)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.bundles.ktor)
    implementation(libs.material)
    implementation(libs.junit)
    implementation(libs.timber)
    kapt(libs.bundles.compiler)
}
