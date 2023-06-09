allprojects {
    afterEvaluate {
        project.apply("$rootDir/gradle/common.gradle")
    }
}

buildscript {
    dependencies {
        classpath(libs.plugin.androidGradle)
        classpath(libs.plugin.kotlin)
        classpath(libs.plugin.secrets.gradle)
    }
}

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.secrets.gradle) apply false
    alias(libs.plugins.serialization) apply false
    alias(libs.plugins.dagger.hilt) apply false
}

tasks {
    register("clean", Delete::class) {
        delete(rootProject.buildDir)
    }
}

apply {
    from("gradle/projectDependencyGraph.gradle")
}
