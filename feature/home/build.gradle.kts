@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.konan.properties.Properties

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.secrets.gradle)
}

android {
    namespace = "org.prography.home"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
        buildConfigField("String", "NAVER_API_MAP_KEY", properties.getProperty("NAVER_API_MAP_KEY"))
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
    implementation(project(":core:utility"))
    implementation(project(":core:base"))
    implementation(project(":core:designsystem"))

    implementation(libs.material)
    implementation(libs.bundles.androidx.compose)
    implementation(libs.naver.map)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso)
}
