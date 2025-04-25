pluginManagement {
    repositories {
        google ()
        mavenCentral()
        gradlePluginPortal()
        // Añade este repositorio específico para KSP
        maven { url = uri("https://repo1.maven.org/maven2/") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "examen3"
include(":app")
 