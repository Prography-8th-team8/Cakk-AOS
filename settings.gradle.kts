pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://naver.jfrog.io/artifactory/maven/")
    }
}
rootProject.name = "Cakk"
include("app")
include(":data")
include(":feature:onboarding")
include(":feature:home")
include(":core:network")
include(":core:utility")
include(":core:base")
include(":core:designsystem")
include(":feature:splash")
include(":domain")
include(":core:localdb")
include(":feature:feed")
