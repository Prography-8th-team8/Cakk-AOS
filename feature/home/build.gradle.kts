@file:Suppress("UnstableApiUsage")

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.secrets.gradle)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.dagger.hilt)
}

android {
    namespace = "org.prography.home"

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
    implementation(project(":data"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:utility"))
    implementation(project(":core:base"))
    implementation(project(":core:network"))

    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.compose.hilt.navigation)
    implementation(libs.androidx.compose.navigation)
    implementation(libs.material)
    implementation(libs.bundles.dagger)
    implementation(libs.bundles.androidx.compose)
    implementation(libs.naver.map)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso)
    kapt(libs.bundles.compiler)
}
