pluginManagement {
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
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
//        maven {url = uri("https://chaquo.com/maven/")}
        maven {url = uri("https://jitpack.io") }
        jcenter() // Warning: this repository is going to shut down soon
    }
}

rootProject.name = "BioVaultWatch"
include(":app")
