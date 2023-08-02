@file:Suppress("UnstableApiUsage")

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.dagger.hilt)
}

android {
    namespace = "org.prography.localdb"

    defaultConfig {
        buildConfigField("String", "FILTER_PREFERENCES_NAME", gradleLocalProperties(rootDir).getProperty("FILTER_PREFERENCES_NAME"))
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.dagger.hilt.android)
    implementation(libs.androidx.datastore)
    implementation(libs.bundles.androidx.room)
    kapt(libs.androidx.room.compiler)
    kapt(libs.dagger.hilt.compiler)
}
