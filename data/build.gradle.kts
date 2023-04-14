@file:Suppress("UnstableApiUsage")

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
}

android {
    namespace = "org.prography.cakk.data"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}
dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:utility"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.junit)
    implementation(libs.androidx.test.junit)
    implementation(libs.androidx.test.espresso)
}
