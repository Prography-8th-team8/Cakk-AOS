@file:Suppress("UnstableApiUsage")

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.dagger.hilt)
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
    implementation(libs.dagger.hilt.android)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.bundles.ktor)
    implementation(libs.material)
    implementation(libs.junit)
    implementation(libs.androidx.test.junit)
    implementation(libs.androidx.test.espresso)
    kapt(libs.bundles.compiler)
}
