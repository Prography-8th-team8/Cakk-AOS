plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "org.prography.utility"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {

    implementation(libs.timber)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
