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
        maven {
            url=uri("https://maven.google.com/")
        }
        maven{
            url=uri("https://www.jitpack.io")
        }
        maven {
            url=uri ("https://mvnrepository.com/artifact/com.android.tools.lint/lint-gradle-api")
        }

    }
}

rootProject.name = "SteelsenseTestApp"
include(":app")
