plugins {
    alias(libs.plugins.personalnotes.android.feature)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "dev.robbik.personalnotes.feature.main"

    buildFeatures { viewBinding = true }
}

dependencies {
    implementation(project(":core:task"))
}