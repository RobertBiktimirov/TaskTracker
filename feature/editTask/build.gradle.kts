plugins {
    alias(libs.plugins.personalnotes.android.feature)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "dev.robbik.personalnotes.feature.taskEdit"
}

dependencies {
    implementation(project(":core:task"))
    implementation(project(":core:notification"))
}