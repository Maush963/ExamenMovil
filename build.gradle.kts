// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Plugin base de la aplicación Android
    alias(libs.plugins.android.application) apply false
    // Plugin base de Kotlin para Android
    alias(libs.plugins.kotlin.android) apply false
    // Plugin de Hilt para inyección de dependencias (necesario para app)
    alias(libs.plugins.hilt.android) apply false
    // Plugin KSP para procesamiento de anotaciones (usado por Hilt)
    alias(libs.plugins.ksp) apply false
}