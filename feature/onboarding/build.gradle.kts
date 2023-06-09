@file:Suppress("UnstableApiUsage")

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.ktlint)
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "org.prography.onboarding"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidxComposeCompiler.get()
    }
    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {
    implementation(project(":core:utility"))
    implementation(project(":core:base"))
    implementation(project(":core:designsystem"))
    implementation(project(":domain"))

    implementation(libs.material)
    implementation(libs.bundles.androidx.compose)
    implementation(libs.bundles.androidx.lifeycle)
    implementation(libs.androidx.compose.hilt.navigation)
    testImplementation(libs.junit)
    implementation(libs.dagger.hilt.android)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso)
    kapt(libs.bundles.compiler)
}
