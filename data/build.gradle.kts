@file:Suppress("UnstableApiUsage")

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt)
}

android {
    namespace = "org.prography.cakk.data"
}
dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:utility"))
    implementation(project(":core:localdb"))
    implementation(project(":domain"))

    implementation(libs.gson)
    implementation(libs.hilt.android)
    implementation(libs.hilt.android.testing)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.dataStore.preferences)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.ktor.android)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.logging)
    implementation(libs.material)
    implementation(libs.junit4)
    implementation(libs.androidx.test.ext)
    implementation(libs.androidx.test.espresso.core)
    kapt(libs.hilt.compiler)
}
