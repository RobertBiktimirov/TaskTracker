pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "PersonalNotes"
include(":app")
include(":feature:main")
include(":feature:editTask")
include(":feature:calendar")
include(":core:database")
include(":core:ui")
include(":core:di")
include(":core:navigation")
include(":core:task")
include(":core:notification")
